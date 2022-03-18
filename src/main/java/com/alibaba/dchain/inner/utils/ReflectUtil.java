package com.alibaba.dchain.inner.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 开帆
 * @date 2022/02/17
 */
public final class ReflectUtil {

    private ReflectUtil() {
    }

    public static List<Field> getAllField(Class clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        List<Field> result = new ArrayList<>(Arrays.asList(declaredFields));

        Class superclass = clazz.getSuperclass();
        while (superclass != null && superclass != Object.class) {
            result.addAll(Arrays.asList(superclass.getDeclaredFields()));
            superclass = superclass.getSuperclass();
        }

        return result;
    }
}
