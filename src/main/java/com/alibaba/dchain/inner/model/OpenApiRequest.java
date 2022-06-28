package com.alibaba.dchain.inner.model;

import java.io.Serializable;
import java.util.Map;

/**
 * @author : 采和
 * @date : 2021/12/14
 */
public interface OpenApiRequest<T extends OpenApiResponse> extends Serializable, Model {

    /**
     * 获取请求所需query入参
     *
     * @return
     */
    Map<String, Object> getQueryParam();

    /**
     * 获取请求所需Body入参
     *
     * @return
     */
    Object getBodyParam();

    /**
     * 获取请求所需header入参
     *
     * @return
     */
    Map<String, Object> getHeaderParam();

    /**
     * ApiResponse的类信息
     *
     * @return T
     */
    Class<T> getResponseClass();

}
