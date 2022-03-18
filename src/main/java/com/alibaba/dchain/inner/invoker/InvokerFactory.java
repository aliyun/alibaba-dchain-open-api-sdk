package com.alibaba.dchain.inner.invoker;

import com.alibaba.dchain.api.ClientConfig;
import com.alibaba.dchain.api.pop.PopClientConfig;
import com.alibaba.dchain.inner.exception.ErrorEnum;
import com.alibaba.dchain.inner.exception.OpenApiException;

/**
 * @author 开帆
 * @date 2022/02/16
 */
public class InvokerFactory {

    public static OpenApiInvoker build(ClientConfig config) throws OpenApiException {
        if (config instanceof PopClientConfig) {
            return new PopInvoker((PopClientConfig)config);
        }
        throw new OpenApiException(ErrorEnum.CLIENT_CONFIG_NOT_SUPPORT);
    }
}
