package com.alibaba.dchain.inner.exception;

import java.util.Map;

/**
 * @author : 采和
 * @date : 2021/12/14
 */
public class OpenApiException extends Exception {

    private static final long serialVersionUID = -737091702287177361L;

    private String errorCode;

    private String errorMsg;

    private Map<String, Object> data;

    public OpenApiException() {
        super();
    }

    public OpenApiException(String message, Throwable cause) {
        super(message, cause);
        errorMsg = message;
    }

    public OpenApiException(String message) {
        super(message);
        errorMsg = message;
    }

    public OpenApiException(Throwable cause) {
        super(cause);
    }

    public OpenApiException(String errorCode, String errorMsg) {
        super(errorCode + ":" + errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public OpenApiException(String errorCode, String errorMsg, Throwable cause) {
        super(errorCode + ":" + errorMsg, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public OpenApiException(String errorCode, String errorMsg, Map<String, Object> data, Throwable cause) {
        super(errorCode + ":" + errorMsg, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    public OpenApiException(ErrorEnum errorEnum) {
        super(errorEnum.getCode() + ":" + errorEnum.getErrMsg());
        this.errorCode = errorEnum.getCode();
        this.errorMsg = errorEnum.getErrMsg();
    }

    public OpenApiException(ErrorEnum errorEnum, Throwable cause) {
        super(errorEnum.getCode() + ":" + errorEnum.getErrMsg(), cause);
        this.errorCode = errorEnum.getCode();
        this.errorMsg = errorEnum.getErrMsg();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
