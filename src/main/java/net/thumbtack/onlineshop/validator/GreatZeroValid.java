package net.thumbtack.onlineshop.validator;

import net.thumbtack.onlineshop.validator.annotation.GreatZero;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GreatZeroValid implements ConstraintValidator<GreatZero, Integer> {

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {
        if(id != null) {
            return id > -1;
        } else {
            return true;
        }
    }
}

