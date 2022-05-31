package com.alibaba.dchain.inner.enums;

/**
 * @author 开帆
 * @date 2022/04/22
 */
public enum SystemHeaderEnum {
    /**
     * 实际调用的EndPoint
     */
    ENDPOINT("x-alibaba-dchain-endpoint"),
    /**
     * 网关类型
     */
    GATEWAY_TYPE("x-alibaba-dchain-gateway-type"),
    /**
     * POP产品code（API注册在POP网关时传递）
     */
    PRODUCT_CODE("x-alibaba-pop-product-code"),
    /**
     * Dchain产品code
     */
    DCHAIN_PRODUCT_CODE("x-alibaba-dchain-product-code"),
    /**
     * 供应链api code
     */
    DCHAIN_API_CODE("x-alibaba-dchain-api-code")
    ;

    private final String code;

    SystemHeaderEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
