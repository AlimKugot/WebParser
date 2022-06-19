package com.alim.mailsender.config;

import com.alim.mailsender.validators.CompositeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class ValidatorConfig implements WebMvcConfigurer {

    @Autowired
    private CompositeValidator compositeValidator;

    @Override
    public Validator getValidator() {
        return compositeValidator;
    }
}
