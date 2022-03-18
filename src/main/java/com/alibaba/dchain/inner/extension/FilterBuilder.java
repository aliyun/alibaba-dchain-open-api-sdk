package com.alibaba.dchain.inner.extension;

import java.util.List;

import com.alibaba.dchain.api.spi.InvokeFilter;
import com.alibaba.dchain.inner.exception.OpenApiException;
import com.alibaba.dchain.inner.invoker.OpenApiInvoker;
import com.alibaba.dchain.inner.model.OpenApiRequest;
import com.alibaba.dchain.inner.model.OpenApiResponse;

/**
 * @author 开帆
 * @date 2022/03/14
 */
public class FilterBuilder {

    public static List<InvokeFilter> getInvokeFilterList() throws OpenApiException {
        ExtensionLoader<InvokeFilter> extensionLoader = new ExtensionLoader<>(InvokeFilter.class);
        return extensionLoader.getExtensionObject();
    }

    public static OpenApiInvoker buildInvokerChain(OpenApiInvoker invoker, List<InvokeFilter> filterList) {
        OpenApiInvoker last = invoker;
        for (int i = filterList.size() - 1; i >= 0; i--) {
            InvokeFilter invokeFilter = filterList.get(i);
            OpenApiInvoker next = last;
            last = new OpenApiInvoker() {
                @Override
                public <T extends OpenApiResponse> T invoke(OpenApiRequest<T> request) throws OpenApiException {
                    return invokeFilter.invoke(next, request);
                }
            };
        }
        return last;
    }
}
