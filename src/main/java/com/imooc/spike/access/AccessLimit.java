package com.imooc.spike.access;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: rammelzzz
 * @Description: 使用注解来防止代码对核心业务代码的侵入
 * @Date: Created in 下午9:37 18-4-21
 * @Modified By:
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {

    int seconds();

    int maxCount();

    boolean needLogin() default true;

}
