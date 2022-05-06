package com.alibaba.dchain.inner.invoker;

import java.util.Map;
import java.util.Objects;

import com.alibaba.dchain.api.pop.PopClientConfig;
import com.alibaba.dchain.inner.converter.PopRequestConverter;
import com.alibaba.dchain.inner.converter.PopResponseConverter;
import com.alibaba.dchain.inner.enums.BodyTypeEnum;
import com.alibaba.dchain.inner.enums.GatewayTypeEnum;
import com.alibaba.dchain.inner.enums.SystemHeaderEnum;
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

    private PopClientConfig config;

    public PopInvoker(PopClientConfig popClientConfig) throws OpenApiException {

        this.config = popClientConfig;

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

        Map<String, String> headers = openApiRequest.getHeaders();
        headers.put(SystemHeaderEnum.PRODUCT_CODE.getCode(), popApiInfo.getProductCode());
        headers.put(SystemHeaderEnum.GATEWAY_TYPE.getCode(), GatewayTypeEnum.POP.name());
        headers.put(SystemHeaderEnum.ENDPOINT.getCode(), config.getEndpoint());
        headers.put(SystemHeaderEnum.DCHAIN_API_CODE.getCode(), popApiInfo.getDchainApiCode());

        try {
            Map<String, ?> resultMap = callApi(popApiInfo, openApiRequest);
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

    private Map<String, ?> callApi(PopApiInfo popApiInfo,
        com.aliyun.teaopenapi.models.OpenApiRequest openApiRequest) throws Exception {
        if (Objects.equals(popApiInfo.getReqBodyType(), BodyTypeEnum.JSON.getCode())) {
            return doROARequest(popApiInfo, openApiRequest);
        } else if (Objects.equals(popApiInfo.getReqBodyType(), BodyTypeEnum.FORM.getCode())) {
            return doROARequestWithForm(popApiInfo, openApiRequest);
        }
        throw new OpenApiException(ErrorEnum.BODY_TYPE_NOT_SUPPORTED);
    }

    private Map<String, ?> doROARequest(PopApiInfo popApiInfo,
        com.aliyun.teaopenapi.models.OpenApiRequest openApiRequest) throws Exception {
        return client.doROARequest(
            popApiInfo.getApiCode(),
            popApiInfo.getApiVersion(),
            popApiInfo.getHttpProtocol(),
            popApiInfo.getHttpMethod(),
            popApiInfo.getAuthType(),
            popApiInfo.getPath(),
            popApiInfo.getBodyType(),
            openApiRequest,
            new RuntimeOptions());
    }

    private Map<String, ?> doROARequestWithForm(PopApiInfo popApiInfo,
        com.aliyun.teaopenapi.models.OpenApiRequest openApiRequest) throws Exception {
        return client.doROARequestWithForm(
            popApiInfo.getApiCode(),
            popApiInfo.getApiVersion(),
            popApiInfo.getHttpProtocol(),
            popApiInfo.getHttpMethod(),
            popApiInfo.getAuthType(),
            popApiInfo.getPath(),
            popApiInfo.getBodyType(),
            openApiRequest,
            new RuntimeOptions());
    }

}
