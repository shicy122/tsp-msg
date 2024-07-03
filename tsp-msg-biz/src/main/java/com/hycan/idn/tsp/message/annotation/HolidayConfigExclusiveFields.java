package com.hycan.idn.tsp.message.annotation;

import com.hycan.idn.tsp.message.validator.HolidayConfigFieldsValidator;
import com.hycan.idn.tsp.message.validator.ResourceConfigFieldsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 资源配置字段校验注解
 *
 * @author shichongyingr
 * @datetime 2024-02-29 10:35
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HolidayConfigFieldsValidator.class)
public @interface HolidayConfigExclusiveFields {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<?> targetClass();
}