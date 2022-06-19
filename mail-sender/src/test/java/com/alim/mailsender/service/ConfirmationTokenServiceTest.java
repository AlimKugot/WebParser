package com.alim.mailsender.service;

import com.alim.mailsender.exception.BusinessException;
import com.alim.mailsender.factory.UuidFactory;
import com.alim.mailsender.model.ConfirmationTokenEntity;
import com.alim.mailsender.model.MailEntity;
import com.alim.mailsender.repository.ConfirmationTokenRepository;
import com.alim.mailsender.service.impl.ConfirmationTokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConfirmationTokenServiceTest {

    private UuidFactory uuidFactory;
    private MailService mailService;
    private ConfirmationTokenRepository tokenRepository;

    private ConfirmationTokenService underTest;

    @BeforeEach
    void setup() {
        uuidFactory = mock(UuidFactory.class);
        mailService = mock(MailService.class);
        tokenRepository = mock(ConfirmationTokenRepository.class);
        underTest = new ConfirmationTokenServiceImpl(uuidFactory, tokenRepository, mailService);
    }


    @Test
    void saveTokenNull() {
        assertThrows(BusinessException.class, () -> underTest.saveToken(null));
    }

    @Test
    void saveTokenIdIsNull() {
        ConfirmationTokenEntity confirmationToken = new ConfirmationTokenEntity();
        assertThrows(BusinessException.class, () -> underTest.saveToken(confirmationToken));
    }

    @Test
    void saveTokenIdIsEmpty() {
        String id = "";
        ConfirmationTokenEntity confirmationToken = new ConfirmationTokenEntity();
        confirmationToken.setId(id);
        assertThrows(BusinessException.class, () -> underTest.saveToken(confirmationToken));
    }

    @Test
    void saveTokenIdLengthIsNotEqualsUuidLength() {
        String id = "some-id";
        ConfirmationTokenEntity confirmationToken = new ConfirmationTokenEntity();
        confirmationToken.setId(id);
        when(uuidFactory.uuid()).thenReturn(UUID.randomUUID().toString());
        assertThrows(BusinessException.class, () -> underTest.saveToken(confirmationToken));
    }

    @Test
    void saveTokenIdIsNotFound() {
        String id = "some-id";
        ConfirmationTokenEntity token = new ConfirmationTokenEntity();
        token.setId(id);

        when(uuidFactory.uuid()).thenReturn(id);
        when(tokenRepository.existsById(id)).thenReturn(true);
        assertThrows(BusinessException.class, () -> underTest.saveToken(token));
    }

    @Test
    void saveTokenSuccessfully() {
        String id = "some-id";
        ConfirmationTokenEntity token = new ConfirmationTokenEntity();
        token.setId(id);

        when(uuidFactory.uuid()).thenReturn(id);
        underTest.saveToken(token);
        verify(tokenRepository, times(1)).save(token);
    }

    @Test
    void getNewToken() {
        String token = "some-token";
        String email = "some@gmail.com";
        when(uuidFactory.uuid()).thenReturn(token);
        ConfirmationTokenEntity tokenEntity = underTest.getNewToken(email);

        assertNotNull(tokenEntity);
        assertEquals(email, tokenEntity.getEmail());
        assertEquals(token, tokenEntity.getToken());
        long diffMinutes = ChronoUnit.MINUTES.between(tokenEntity.getCreatedAt(), tokenEntity.getExpiresAt());
        assertTrue(diffMinutes >= 0);
        assertNull(tokenEntity.getConfirmedAt());
    }


    @Test
    void confirmTokenIsNotExists() {
        assertThrows(BusinessException.class, () -> underTest.confirmToken(null));
    }

    @Test
    void confirmTokenCreatedAtIsNull() {
        String token = "some-token";
        ConfirmationTokenEntity tokenEntity = new ConfirmationTokenEntity();
        tokenEntity.setToken(token);
        tokenEntity.setConfirmedAt(LocalDateTime.MIN);

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(tokenEntity));
        assertThrows(BusinessException.class, () -> underTest.confirmToken(token));
    }

    @Test
    void confirmTokenAlreadyConfirmed() {
        String token = "some-token";
        ConfirmationTokenEntity tokenEntity = new ConfirmationTokenEntity();
        tokenEntity.setToken(token);
        tokenEntity.setCreatedAt(LocalDateTime.MIN);
        tokenEntity.setConfirmedAt(LocalDateTime.MAX);

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(tokenEntity));
        assertThrows(BusinessException.class, () -> underTest.confirmToken(token));
    }

    @Test
    void confirmTokenAlreadyConfirmedBeforeCreatedAtDate() {
        String token = "some-token";
        ConfirmationTokenEntity tokenEntity = new ConfirmationTokenEntity();
        tokenEntity.setToken(token);
        tokenEntity.setCreatedAt(LocalDateTime.MAX);
        tokenEntity.setConfirmedAt(LocalDateTime.MIN);

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(tokenEntity));
        assertThrows(BusinessException.class, () -> underTest.confirmToken(token));
    }

    @Test
    void confirmTokenExpiresIsNull() {
        String token = "some-token";
        ConfirmationTokenEntity tokenEntity = new ConfirmationTokenEntity();
        tokenEntity.setCreatedAt(LocalDateTime.now());
        tokenEntity.setToken(token);

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(tokenEntity));
        assertThrows(BusinessException.class, () -> underTest.confirmToken(token));
    }

    @Test
    void confirmTokenExpired() {
        String token = "some-token";
        ConfirmationTokenEntity tokenEntity = new ConfirmationTokenEntity();
        tokenEntity.setToken(token);
        tokenEntity.setCreatedAt(LocalDateTime.MAX);
        tokenEntity.setExpiresAt(LocalDateTime.MIN);

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(tokenEntity));
        assertThrows(BusinessException.class, () -> underTest.confirmToken(token));
    }

    @Test
    void confirmTokenSuccessfully() {
        String token = "some-token";
        String email = "some@gmail.com";
        ConfirmationTokenEntity tokenEntity = new ConfirmationTokenEntity();
        tokenEntity.setEmail(email);
        tokenEntity.setToken(token);
        tokenEntity.setCreatedAt(LocalDateTime.now());
        tokenEntity.setExpiresAt(LocalDateTime.MAX);

        MailEntity mailEntity = new MailEntity();
        when(mailService.getByEmail(email)).thenReturn(mailEntity);
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(tokenEntity));
        underTest.confirmToken(token);

        mailEntity.setEnabled(true);
        verify(mailService, times(1)).update(mailEntity);
    }
}
