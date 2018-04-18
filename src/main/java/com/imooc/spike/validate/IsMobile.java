package com.imooc.spike.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午10:30 18-4-17
 * @Modified By:
 **/
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {IsMobileValidator.class}
)
public @interface IsMobile {

        boolean required() default true;

        String message() default "手机号码格式有误";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
}
