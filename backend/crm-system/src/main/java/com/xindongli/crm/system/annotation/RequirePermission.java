package com.xindongli.crm.system.annotation;

import java.lang.annotation.*;

/**
 * 权限校验注解
 * 用于标注需要权限校验的方法
 * 
 * @author 芯动力科技
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {

    /**
     * 权限标识数组
     * 满足其中一个即可访问
     */
    String[] value() default {};

    /**
     * 是否需要满足所有权限
     * true: 需要满足所有权限
     * false: 满足其中一个即可
     */
    boolean requireAll() default false;

}
