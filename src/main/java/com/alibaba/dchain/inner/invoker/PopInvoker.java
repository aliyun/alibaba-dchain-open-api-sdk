package com.alibaba.dchain.inner.invoker;

import java.util.Map;

import com.alibaba.dchain.api.pop.PopClientConfig;
import com.alibaba.dchain.api.spi.InvokeFilter;
import com.alibaba.dchain.inner.converter.PopRequestConverter;
import com.alibaba.dchain.inner.converter.PopResponseConverter;
import com.alibaba.dchain.inner.exception.ErrorEnum;
import com.alibaba.dchain.inner.exception.OpenApiException;
import com.alibaba.dchain.inner.model.BasePopRequest;
import com.alibaba.dchain.inner.model.BasePopRequest.PopApiInfo;
import com.alibaba.dchain.inner.model.OpenApiRequest;
import com.alibaba.dchain.inner.model.OpenApiResponse;

import com.aliyun.tea.TeaException;
import com.aliyun.tea.TeaUnretryableException;
import com.aliyun.teaopenapi.Client;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;

/**
 * @author : 采和
 * @date : 2021/12/14
 */
public class PopInvoker implements OpenApiInvoker {

    private Client client;

    public PopInvoker(PopClientConfig popClientConfig) throws OpenApiException {
        Config popConfig = new Config();
        popConfig.setEndpoint(popClientConfig.getEndpoint());
        popConfig.setRegionId(popClientConfig.getRegionId());
        popConfig.setAccessKeyId(popClientConfig.getAccessKeyId());
        popConfig.setAccessKeySecret(popClientConfig.getAccessKeySecret());

        try {
            client = new Client(popConfig);
        } catch (Exception e) {
            throw new OpenApiException(ErrorEnum.INIT_POP_CLIENT_ERROR, e);
        }
    }

    @Override
    public <T extends OpenApiResponse> T invoke(OpenApiRequest<T> request) throws OpenApiException {
        if (!(request instanceof BasePopRequest)) {
            throw new OpenApiException(ErrorEnum.REQUEST_NOT_POP_REQUEST);
        }
        PopApiInfo popApiInfo = ((BasePopRequest)request).findPopApiInfo();
        com.aliyun.teaopenapi.models.OpenApiRequest openApiRequest = PopRequestConverter.toPopRequest(request);

        try {
            Map<String, ?> resultMap = client.doROARequest(
                popApiInfo.getApiCode(),
                popApiInfo.getApiVersion(),
                popApiInfo.getHttpProtocol(),
                popApiInfo.getHttpMethod(),
                popApiInfo.getAuthType(),
                popApiInfo.getPath(),
                popApiInfo.getBodyType(),
                openApiRequest,
                new RuntimeOptions());
            return PopResponseConverter.toOpenApiResponse(resultMap, request.getResponseClass());
        } catch (Exception e) {
            if (e instanceof TeaException) {
                TeaException teaException = (TeaException)e;
                throw new OpenApiException(teaException.getCode(), teaException.getMessage(), teaException.getData(),
                    teaException.getCause());
            }
            if (e instanceof TeaUnretryableException) {
                TeaUnretryableException teaException = (TeaUnretryableException)e;
                throw new OpenApiException(
                    teaException.getLastRequest() != null ? teaException.getLastRequest().toString()
                        : teaException.getMessage(),
                    teaException.getCause());
            }
            throw new OpenApiException(e);
        }
    }

}
