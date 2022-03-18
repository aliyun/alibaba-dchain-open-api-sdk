package com.alibaba.dchain.inner.converter;

import java.util.Map;

import com.alibaba.dchain.inner.exception.OpenApiException;
import com.alibaba.dchain.inner.model.OpenApiResponse;

/**
 * @author : 采和
 * @date : 2021/12/23
 */
public final class PopResponseConverter {

    private PopResponseConverter() {
    }

    /**
     * Response转换
     *
     * @param map   pop execute result
     * @param clazz response class
     * @param <T>   response class
     * @return response
     * @throws OpenApiException e
     */
    public static <T extends OpenApiResponse> T toOpenApiResponse(Map<String, ?> map, Class<T> clazz)
        throws OpenApiException {
        return ModelConverter.toModel(map, newInstance(clazz));
    }

    private static <T extends OpenApiResponse> T newInstance(Class<T> clazz)
        throws OpenApiException {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new OpenApiException(e);
        }
    }

}
