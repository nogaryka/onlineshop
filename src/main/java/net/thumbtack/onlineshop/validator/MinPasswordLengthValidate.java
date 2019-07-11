package net.thumbtack.onlineshop.validator;

import net.thumbtack.onlineshop.validator.annotation.MaxNameLength;
import net.thumbtack.onlineshop.validator.annotation.MinPasswordLength;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MinPasswordLengthValidate  implements ConstraintValidator<MinPasswordLength, String> {
    private static final String PATH_MIN_PASSWORD_LENGTH = "${min_password_length}";

    @Value(PATH_MIN_PASSWORD_LENGTH)
    private int minPasswordLength;

    @Override
    public boolean isValid(String validField, ConstraintValidatorContext constraintValidatorContext) {
        return minPasswordLength <= validField.length();
    }
}
