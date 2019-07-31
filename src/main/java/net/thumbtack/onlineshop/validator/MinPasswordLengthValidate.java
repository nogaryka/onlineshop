package net.thumbtack.onlineshop.validator;

import net.thumbtack.onlineshop.validator.annotation.MinPasswordLength;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static net.thumbtack.onlineshop.config.ConstConfig.PATH_MIN_PASSWORD_LENGTH;

public class MinPasswordLengthValidate implements ConstraintValidator<MinPasswordLength, String> {


    @Value(PATH_MIN_PASSWORD_LENGTH)
    private int minPasswordLength;

    @Override
    public boolean isValid(String validField, ConstraintValidatorContext constraintValidatorContext) {
        return minPasswordLength <= validField.length();
    }
}
