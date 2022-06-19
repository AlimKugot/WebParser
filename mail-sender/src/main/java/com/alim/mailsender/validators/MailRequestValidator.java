package com.alim.mailsender.validators;

import com.alim.mailsender.dto.request.MailRequestDto;
import com.alim.mailsender.exception.BusinessException;
import org.springframework.stereotype.Component;


@Component
public class MailRequestValidator extends AbstractDtoValidator<MailRequestDto> {

    /**
     * RegExp основанный на стандарте RFC 5322
     */
    private static final String EMAIL_PATTERN = "^[a-zA-Z\\d_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z\\d.-]+$";

    public MailRequestValidator() {
        super(MailRequestDto.class);
    }


    @Override
    public void validate(MailRequestDto dto) {
        if (dto == null) {
            throw new BusinessException("Mail request is null");
        }
        if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
            throw new BusinessException("Email cannot be empty");
        }
        if (!dto.getEmail().matches(EMAIL_PATTERN)) {
            throw new BusinessException("Email is not correct");
        }
    }
}
