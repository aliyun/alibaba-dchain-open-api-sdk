package com.alibaba.dchain.inner.model;

import java.io.Serializable;
import java.util.Map;

/**
 * @author : 采和
 * @date : 2021/12/14
 */
public interface OpenApiResponse extends Model, Serializable {

    /**
     * 响应头
     *
     * @return headers
     */
    Map<String, String> getHeaders();

}
