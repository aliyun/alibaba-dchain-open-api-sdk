package com.alibaba.dchain.inner.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * the annotation for field serialize or deserialize, refer to pop tea's NameInMap
 *
 * @author 开帆
 * @date 2022/02/17
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface NameMapping {
    /**
     * @return the desired name of the field when it is serialized or deserialized
     */
    String value();
}
