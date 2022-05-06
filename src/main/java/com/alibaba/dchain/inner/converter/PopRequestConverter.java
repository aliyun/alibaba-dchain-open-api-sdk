package com.alibaba.dchain.inner.converter;

import java.util.HashMap;

import com.alibaba.dchain.inner.exception.OpenApiException;
import com.alibaba.dchain.inner.model.OpenApiRequest;
import com.alibaba.dchain.inner.utils.MapUtil;

/**
 * @author : 采和
 * @date : 2021/12/19
 */
public final class PopRequestConverter {

    private PopRequestConverter() {
    }

    public static <T extends OpenApiRequest<?>> com.aliyun.teaopenapi.models.OpenApiRequest toPopRequest(T request)
        throws OpenApiException {
        com.aliyun.teaopenapi.models.OpenApiRequest openApiRequest = new com.aliyun.teaopenapi.models.OpenApiRequest();

        if (MapUtil.isNotEmpty(request.getBodyParam())) {
            openApiRequest.setBody(ModelConverter.parseObject(request.getBodyParam()));
        }
        if (MapUtil.isNotEmpty(request.getQueryParam())) {
            openApiRequest.setQuery(ModelConverter.parseMapToMapString((request.getQueryParam())));
        }
        if (MapUtil.isNotEmpty(request.getHeaderParam())) {
            openApiRequest.setHeaders(ModelConverter.parseMapToMapString((request.getHeaderParam())));
        } else {
            openApiRequest.setHeaders(new HashMap<>(16));
        }
        return openApiRequest;
    }

}
