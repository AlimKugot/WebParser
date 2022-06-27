package com.alim.mailsender.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public abstract class AbstractDtoValidator<T> implements Validator {

    protected Class<T> clazz;

    protected AbstractDtoValidator(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return this.clazz == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (clazz.isAssignableFrom(target.getClass())) {
            validate((T) target);
        }
    }

    public abstract void validate(T dto);
}

