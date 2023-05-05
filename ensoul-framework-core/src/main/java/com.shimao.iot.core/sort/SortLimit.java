package com.shimao.iot.core.sort;

import java.lang.annotation.*;

/**
 * 排序限制
 *
 * @author striver.cradle
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface SortLimit {

    /**
     * 同sort规则一样，对客户端请求过来的排序规则进行过滤
     */
    String[] value() default {};

    /**
     * Specifies if sort parameter is required or not.
     */
    boolean required() default false;
}
