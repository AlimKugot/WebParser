package com.alim.mailsender.validators;

import com.alim.mailsender.dto.request.MailRequestDto;
import com.alim.mailsender.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MailRequestValidatorTest {

    private MailRequestValidator underTest;

    @BeforeEach
    void setup() {
        underTest = new MailRequestValidator();
    }

    @Test
    void validateDtoIsNull() {
        assertThrows(BusinessException.class, () -> underTest.validate(null));
    }

    @Test
    void validateDtoGmailIsNull() {
        MailRequestDto requestDto = new MailRequestDto();
        assertThrows(BusinessException.class, () -> underTest.validate(requestDto));
    }

    @Test
    void validateDtoGmailIsEmpty() {
        MailRequestDto requestDto = new MailRequestDto();
        requestDto.setEmail("");
        assertThrows(BusinessException.class, () -> underTest.validate(requestDto));
    }

    @Test
    void validateEmailDoesntMatchToPattern() {
        MailRequestDto requestDto = new MailRequestDto();
        requestDto.setEmail("some-email");
        assertThrows(BusinessException.class, () -> underTest.validate(requestDto));
    }

    @Test
    void validateSuccess() {
        MailRequestDto requestDto = new MailRequestDto();
        requestDto.setEmail("some@gmail.com");
        assertDoesNotThrow(() -> underTest.validate(requestDto));
    }
}
