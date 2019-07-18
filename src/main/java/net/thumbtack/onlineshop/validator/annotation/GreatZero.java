package net.thumbtack.onlineshop.validator.annotation;

import net.thumbtack.onlineshop.validator.GreatZeroValid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GreatZeroValid.class)
public @interface GreatZero {
    String message() default "Value will be greater zero";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
