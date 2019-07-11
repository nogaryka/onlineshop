package net.thumbtack.onlineshop.validator.annotation;

import net.thumbtack.onlineshop.validator.MaxNameLengthValidate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MaxNameLengthValidate.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxNameLength {
    String message() default "The field is very long";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}