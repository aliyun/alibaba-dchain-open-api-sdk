package com.alibaba.dchain.inner.utils;

import java.util.Map;

/**
 * @author 开帆
 * @date 2022/02/17
 */
public final class MapUtil {

    private MapUtil() {
    }

    public static boolean isEmpty(Map map) {
        return (map == null || map.isEmpty());
    }

    public static boolean isNotEmpty(Map map) {
        return !MapUtil.isEmpty(map);
    }
}
