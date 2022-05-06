package com.alibaba.dchain.inner.enums;

/**
 * @author 开帆
 * @date 2022/04/24
 */
public enum BodyTypeEnum {
    /**
     * json 格式
     */
    JSON("json"),
    /**
     * form表单格式
     */
    FORM("form")
    ;

    private String code;

    BodyTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
