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
         * the apiCode
         */
        private String apiCode;
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

        public PopApiInfo() {
        }

        public String getApiCode() {
            return apiCode;
        }

        public void setApiCode(String apiCode) {
            this.apiCode = apiCode;
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
    }
}
