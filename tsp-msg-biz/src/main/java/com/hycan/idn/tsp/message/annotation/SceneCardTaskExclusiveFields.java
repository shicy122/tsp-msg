package com.hycan.idn.tsp.message.annotation;

import com.hycan.idn.tsp.message.validator.SceneCardTaskFieldsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 场景卡片字段校验注解
 *
 * @author shichongyingr
 * @datetime 2024-03-12 10:35
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SceneCardTaskFieldsValidator.class)
public @interface SceneCardTaskExclusiveFields {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<?> targetClass();
}