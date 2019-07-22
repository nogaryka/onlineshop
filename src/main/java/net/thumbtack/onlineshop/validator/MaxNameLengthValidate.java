package net.thumbtack.onlineshop.validator;

import net.thumbtack.onlineshop.exceptions.OnlineShopValidatorException;
import net.thumbtack.onlineshop.validator.annotation.MaxNameLength;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MaxNameLengthValidate implements ConstraintValidator<MaxNameLength, String> {
    private static final String PATH_MAX_NAME_LENGTH = "${max_name_length}";

    @Value(PATH_MAX_NAME_LENGTH)
    private int maxNameLength;

    @Override
    public boolean isValid(String validField, ConstraintValidatorContext constraintValidatorContext) {
        if (maxNameLength < validField.length()) {
            throw new OnlineShopValidatorException(constraintValidatorContext.getDefaultConstraintMessageTemplate());
        }
        return true;
    }
}
