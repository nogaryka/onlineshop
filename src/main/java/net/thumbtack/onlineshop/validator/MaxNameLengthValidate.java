package net.thumbtack.onlineshop.validator;

import net.thumbtack.onlineshop.exceptions.OnlineShopValidatorException;
import net.thumbtack.onlineshop.validator.annotation.MaxNameLength;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static net.thumbtack.onlineshop.config.ConstConfig.PATH_MAX_NAME_LENGTH;

public class MaxNameLengthValidate implements ConstraintValidator<MaxNameLength, String> {

    @Value(PATH_MAX_NAME_LENGTH)
    private int maxNameLength;

    @Override
    public boolean isValid(String validField, ConstraintValidatorContext constraintValidatorContext) {
        if (validField == null || validField.isEmpty()) {
            return true;
        }
        if (maxNameLength < validField.length()) {
            throw new OnlineShopValidatorException(constraintValidatorContext.getDefaultConstraintMessageTemplate());
        }
        return true;
    }
}
