package com.alibaba.dchain.inner.utils;

/**
 * @author 开帆
 * @date 2022/02/17
 */
public final class StringUtil {

    private StringUtil() {
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
