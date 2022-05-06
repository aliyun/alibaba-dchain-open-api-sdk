package com.alibaba.dchain.inner.exception;

/**
 * @author 开帆
 * @date 2022/02/16
 */
public enum ErrorEnum {
    /**
     * 异常码
     */

    CLIENT_CONFIG_NOT_SUPPORT("CLIENT_CONFIG_NOT_SUPPORTED", "this client config is not supported!"),

    INIT_POP_CLIENT_ERROR("INIT_POP_CLIENT_ERROR", "init pop client fail, please check config."),

    REQUEST_NOT_POP_REQUEST("REQUEST_NOT_POP_REQUEST",
        "the request is not based on pop request,but invoked by pop client."),

    PARSE_OBJECT_TO_MAP_ERROR("PARSE_OBJECT_TO_MAP_ERROR", "parse object to map fail."),

    FIELD_TYPE_NOT_SUPPORT("FIELD_TYPE_NOT_SUPPORT", "field's type not supported."),

    INIT_MODEL_ERROR("INIT_MODEL_ERROR", "init model fail"),

    SET_FIELD_ERROR("SET_FIELD_ERROR", "set field value fail"),

    LOAD_SPI_ERROR("LOAD_SPI_ERROR", "load spi provider fail"),

    INSTANTIATE_INSTANCE_ERROR("INSTANTIATE_INSTANCE_ERROR", "instantiate class instance error"),

    BODY_TYPE_NOT_SUPPORTED("BODY_TYPE_NOT_SUPPORTED", "body type not supported"),

    ;

    private String code;

    private String errMsg;

    ErrorEnum(String code, String errMsg) {
        this.code = code;
        this.errMsg = errMsg;
    }

    public String getCode() {
        return code;
    }

    public String getErrMsg() {
        return errMsg;
    }
}
