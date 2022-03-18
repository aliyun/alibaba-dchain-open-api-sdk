package com.alibaba.dchain.inner.model;

import com.alibaba.dchain.inner.exception.OpenApiException;

/**
 * mark class as model, which should be mapping according to NameInMap annotation
 *
 * @author 开帆
 * @date 2022/02/17
 */
public interface Model {

    /**
     * 客户端前置校验
     *
     * @throws OpenApiException e
     */
    void validate() throws OpenApiException;
}
