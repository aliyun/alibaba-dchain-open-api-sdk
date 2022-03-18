package com.alibaba.dchain.api.spi;

import com.alibaba.dchain.inner.exception.OpenApiException;
import com.alibaba.dchain.inner.invoker.OpenApiInvoker;
import com.alibaba.dchain.inner.model.OpenApiRequest;
import com.alibaba.dchain.inner.model.OpenApiResponse;

/**
 * 调用filter，业务自定义实现，去拦截处理对网关的调用
 *
 * @author 开帆
 * @date 2022/03/09
 */
public interface InvokeFilter {

    /**
     * 调用服务
     *
     * @param invoker 调用器，filter都会包装成调用器
     * @param request 请求参数
     * @param <T>
     * @return
     */
    <T extends OpenApiResponse> T invoke(OpenApiInvoker invoker, OpenApiRequest<T> request) throws OpenApiException;
}
