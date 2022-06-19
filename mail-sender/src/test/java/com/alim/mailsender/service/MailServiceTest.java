package com.alim.mailsender.service;

import com.alim.mailsender.dto.request.MailRequestDto;
import com.alim.mailsender.exception.BusinessException;
import com.alim.mailsender.factory.UuidFactory;
import com.alim.mailsender.model.MailEntity;
import com.alim.mailsender.repository.MailRepository;
import com.alim.mailsender.service.impl.MailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MailServiceTest {

    private final MailEntity mailEntity = new MailEntity();
    private final MailRequestDto mailDto = new MailRequestDto();
    private MailRepository mailRepository;
    private UuidFactory uuidFactory;
    private MailServiceImpl underTest;

    @BeforeEach
    void setup() {
        mailRepository = mock(MailRepository.class);
        uuidFactory = mock(UuidFactory.class);
        underTest = new MailServiceImpl(uuidFactory, mailRepository);
    }

    @Test
    void updateNull() {
        assertThrows(BusinessException.class, () -> underTest.update(null));
    }

    @Test
    void updateEmailIsNull() {
        mailEntity.setEmail(null);
        assertThrows(BusinessException.class, () -> underTest.update(mailEntity));
    }

    @Test
    void updateEmailIsEmpty() {
        mailEntity.setId("some-id");
        mailEntity.setEmail("");
        assertThrows(BusinessException.class, () -> underTest.update(mailEntity));
    }


    @Test
    void updateEmailNotFound() {
        mailEntity.setId("some-id");
        mailEntity.setEmail("bad_gmail.com");
        assertThrows(BusinessException.class, () -> underTest.update(mailEntity));
    }

    @Test
    void updateSuccessfully() {
        mailEntity.setId("some-id");
        mailEntity.setEmail("some@gmail.com");
        when(mailRepository.existsById(mailEntity.getId())).thenReturn(true);
        assertDoesNotThrow(() -> underTest.update(mailEntity));
        verify(mailRepository, times(1)).deleteById(mailEntity.getId());
        verify(mailRepository, times(1)).save(mailEntity);
    }


    @Test
    void saveNull() {
        assertThrows(BusinessException.class, () -> underTest.save(null));
    }

    @Test
    void saveExistingEmail() {
        String email = "example@gmail.com";
        mailDto.setEmail(email);
        when(mailRepository.existsByEmail(email)).thenReturn(true);
        assertThrows(BusinessException.class, () -> underTest.save(mailDto));
    }

    @Test
    void saveSuccessfully() {
        String id = "12345-some-uuid";
        String email = "example@gmail.com";
        mailDto.setEmail(email);

        MailEntity expected = MailEntity.builder()
                .id(id)
                .email(email)
                .isEnabled(false)
                .build();

        when(uuidFactory.uuid()).thenReturn("12345-some-uuid");
        MailEntity actual = underTest.save(mailDto);

        assertEquals(expected, actual);
        verify(mailRepository, times(1)).save(expected);
    }

    @Test
    void getByEmailNull() {
        assertThrows(BusinessException.class, () -> underTest.getByEmail(null));
    }


    @Test
    void getByEmailSuccess() {
        String email = "some@gmail.com";
        mailEntity.setEmail(email);
        when(mailRepository.findByEmail(email)).thenReturn(Optional.of(mailEntity));
        MailEntity actual = underTest.getByEmail(email);
        assertEquals(mailEntity, actual);
        verify(mailRepository, times(1)).findByEmail(email);
    }


    @Test
    void getAllSuccess() {
        List<MailEntity> expected = List.of(mailEntity);
        when(mailRepository.findAll()).thenReturn(expected);
        List<MailEntity> actual = underTest.getAll();
        assertEquals(expected, actual);
    }
}
