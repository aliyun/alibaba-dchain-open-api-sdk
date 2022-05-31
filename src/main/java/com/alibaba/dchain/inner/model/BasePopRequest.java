package com.alibaba.dchain.inner.model;

/**
 * the request for pop
 *
 * @author 开帆
 * @date 2022/02/17
 */
public abstract class BasePopRequest<T extends OpenApiResponse> implements OpenApiRequest<T> {

    protected PopApiInfo popApiInfo;

    /**
     * 获取pop系统参数，不使用get方法，避免被子类覆盖
     *
     * @return
     */
    public PopApiInfo findPopApiInfo() {
        return popApiInfo;
    }

    public static class PopApiInfo {
        /**
         * the product code of pop
         */
        private String productCode;
        /**
         * the product code of dchain
         */
        private String dchainProductCode;
        /**
         * the api code of pop gateway
         */
        private String apiCode;
        /**
         * the api code of dchain gateway
         */
        private String dchainApiCode;
        /**
         * the api version
         */
        private String apiVersion;
        /**
         * http protocol,like: http、https
         */
        private String httpProtocol;
        /**
         * http method, like: get、post、delete、etc
         */
        private String httpMethod;
        /**
         * pop request path
         */
        private String path;
        /**
         * auth type, like: AK
         */
        private String authType;
        /**
         * body type, like: json
         */
        private String bodyType;
        /**
         * the body type of request, like: json、form
         */
        private String reqBodyType;

        public PopApiInfo() {
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getDchainProductCode() {
            return dchainProductCode;
        }

        public void setDchainProductCode(String dchainProductCode) {
            this.dchainProductCode = dchainProductCode;
        }

        public String getApiCode() {
            return apiCode;
        }

        public void setApiCode(String apiCode) {
            this.apiCode = apiCode;
        }

        public String getDchainApiCode() {
            return dchainApiCode;
        }

        public void setDchainApiCode(String dchainApiCode) {
            this.dchainApiCode = dchainApiCode;
        }

        public String getApiVersion() {
            return apiVersion;
        }

        public void setApiVersion(String apiVersion) {
            this.apiVersion = apiVersion;
        }

        public String getHttpProtocol() {
            return httpProtocol;
        }

        public void setHttpProtocol(String httpProtocol) {
            this.httpProtocol = httpProtocol;
        }

        public String getHttpMethod() {
            return httpMethod;
        }

        public void setHttpMethod(String httpMethod) {
            this.httpMethod = httpMethod;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getAuthType() {
            return authType;
        }

        public void setAuthType(String authType) {
            this.authType = authType;
        }

        public String getBodyType() {
            return bodyType;
        }

        public void setBodyType(String bodyType) {
            this.bodyType = bodyType;
        }

        public String getReqBodyType() {
            return reqBodyType;
        }

        public void setReqBodyType(String reqBodyType) {
            this.reqBodyType = reqBodyType;
        }
    }
}
