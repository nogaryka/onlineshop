package net.thumbtack.onlineshop.validator.annotation;

import net.thumbtack.onlineshop.validator.MinPasswordLengthValidate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MinPasswordLengthValidate.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MinPasswordLength {
    String message() default "The password is very short";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
