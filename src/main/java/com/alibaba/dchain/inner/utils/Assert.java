package com.alibaba.dchain.inner.utils;

import com.alibaba.dchain.inner.exception.ErrorEnum;
import com.alibaba.dchain.inner.exception.OpenApiException;

/**
 * @author 开帆
 * @date 2022/02/18
 */
public final class Assert {

    private Assert() {
    }

    /**
     * Asserts that a condition is true. If it isn't it throws an
     * {@link OpenApiException} without a message.
     *
     * @param condition condition to be checked
     * @param errorEnum error message
     */
    public static void assertTrue(boolean condition, ErrorEnum errorEnum) throws OpenApiException {
        if (!condition) {
            fail(errorEnum);
        }
    }

    /**
     * Asserts that two objects are equal. If they are not, an
     * {@link OpenApiException} without a message is thrown. If
     * <code>expected</code> and <code>actual</code> are <code>null</code>,
     * they are considered equal.
     *
     * @param expected expected value
     * @param actual the value to check against <code>expected</code>
     * @param errorEnum error message
     * @throws OpenApiException exception will be threw with OpenApiException
     */
    public static void assertEquals(Object expected, Object actual, ErrorEnum errorEnum) throws OpenApiException {
        if (equalsRegardingNull(expected, actual)) {
            return;
        }
        fail(errorEnum);
    }

    private static boolean equalsRegardingNull(Object expected, Object actual) {
        if (expected == null) {
            return actual == null;
        }

        return isEquals(expected, actual);
    }

    private static boolean isEquals(Object expected, Object actual) {
        return expected.equals(actual);
    }

    private static void fail(ErrorEnum errorEnum) throws OpenApiException {
        throw new OpenApiException(errorEnum);
    }
}
