package com.alibaba.dchain.inner.utils;

import java.util.Collection;

/**
 * @author 开帆
 * @date 2022/03/15
 */
public class CollectionUtil {

    /**
     * Null-safe check if the specified collection is empty.
     * <p>
     * Null returns true.
     *
     * @param collection the collection to check, may be null
     * @return true if empty or null
     * @since Commons Collections 3.2
     */
    public static boolean isEmpty(Collection collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * Null-safe check if the specified collection is not empty.
     * <p>
     * Null returns false.
     *
     * @param collection the collection to check, may be null
     * @return true if non-null and non-empty
     * @since Commons Collections 3.2
     */
    public static boolean isNotEmpty(Collection collection) {
        return !CollectionUtil.isEmpty(collection);
    }
}
