package com.alibaba.dchain.api;

import java.util.List;

import com.alibaba.dchain.api.spi.InvokeFilter;
import com.alibaba.dchain.inner.exception.OpenApiException;
import com.alibaba.dchain.inner.extension.FilterBuilder;
import com.alibaba.dchain.inner.invoker.InvokerFactory;
import com.alibaba.dchain.inner.invoker.OpenApiInvoker;
import com.alibaba.dchain.inner.model.OpenApiRequest;
import com.alibaba.dchain.inner.model.OpenApiResponse;
import com.alibaba.dchain.inner.utils.CollectionUtil;

/**
 * @author : 采和
 * @date : 2021/12/14
 */
public class OpenApiClient {

    private final OpenApiInvoker openApiInvoker;

    public OpenApiClient(ClientConfig config) throws OpenApiException {
        OpenApiInvoker realInvoker = InvokerFactory.build(config);
        List<InvokeFilter> filterList = FilterBuilder.getInvokeFilterList();
        if (CollectionUtil.isNotEmpty(filterList)) {
            openApiInvoker = FilterBuilder.buildInvokerChain(realInvoker, filterList);
        } else {
            openApiInvoker = realInvoker;
        }
    }

    /**
     * OpenApi调用执行入口
     *
     * @param request OpenApiRequest
     * @param <T>     T of OpenApiResponse
     * @return T
     * @throws OpenApiException e
     */
    public <T extends OpenApiResponse> T execute(OpenApiRequest<T> request) throws OpenApiException {
        request.validate();
        return openApiInvoker.invoke(request);
    }
}
