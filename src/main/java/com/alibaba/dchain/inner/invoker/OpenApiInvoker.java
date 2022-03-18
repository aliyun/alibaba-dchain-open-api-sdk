package com.alibaba.dchain.inner.invoker;

import com.alibaba.dchain.inner.model.OpenApiRequest;
import com.alibaba.dchain.inner.model.OpenApiResponse;
import com.alibaba.dchain.inner.exception.OpenApiException;

/**
 * @author : 采和
 * @date : 2021/12/14
 */
public interface OpenApiInvoker {

    /**
     * 网关接口调用器
     *
     * @param request  OpenApiRequest
     * @return OpenApiResponse
     * @throws OpenApiException e
     */
    <T extends OpenApiResponse> T invoke(OpenApiRequest<T> request) throws OpenApiException;

}
