package com.alim.mailsender.validators;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class CompositeValidator implements Validator {

    private static final Map<Class<?>, Validator> VALIDATOR_MAP = new HashMap<>();

    @Autowired
    private List<AbstractDtoValidator<?>> validators;

    @PostConstruct
    public void init() {
        for (AbstractDtoValidator<?> validator : validators) {
            VALIDATOR_MAP.put(validator.clazz, validator);
        }
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return VALIDATOR_MAP.containsKey(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Validator dtoValidator = VALIDATOR_MAP.get(target.getClass());
        if (dtoValidator != null) {
            dtoValidator.validate(target, errors);
        }
    }

}

