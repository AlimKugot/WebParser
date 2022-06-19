package com.alim.mailsender.service.impl;

import com.alim.mailsender.exception.BusinessException;
import com.alim.mailsender.factory.UuidFactory;
import com.alim.mailsender.model.ConfirmationTokenEntity;
import com.alim.mailsender.model.MailEntity;
import com.alim.mailsender.repository.ConfirmationTokenRepository;
import com.alim.mailsender.service.ConfirmationTokenService;
import com.alim.mailsender.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final UuidFactory uuidFactory;
    private final ConfirmationTokenRepository tokenRepository;
    private final MailService mailService;

    @Override
    public void saveToken(ConfirmationTokenEntity confirmationToken) {
        if (confirmationToken == null)
            throw new BusinessException("Token is null");
        if (confirmationToken.getId() == null || confirmationToken.getId().isEmpty()) {
            throw new BusinessException("Token's id is null or empty");
        }
        // In Java string.length() has O(1)
        if (confirmationToken.getId().length() != uuidFactory.uuid().length()) {
            throw new BusinessException("Token's length is not equals to Uuid's length");
        }
        if (tokenRepository.existsById(confirmationToken.getId())) {
            throw new BusinessException("Token already exists");
        }
        tokenRepository.save(confirmationToken);
    }

    @Override
    public ConfirmationTokenEntity getNewToken(String email) {
        String token = uuidFactory.uuid();
        return new ConfirmationTokenEntity(
                email,
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15)
        );
    }


    @Override
    @Transactional
    public void confirmToken(String token) {
        ConfirmationTokenEntity tokenEntity = tokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new BusinessException("token not found"));

        LocalDateTime createdAt = tokenEntity.getCreatedAt();
        LocalDateTime confirmedAt = tokenEntity.getConfirmedAt();
        LocalDateTime expiresAt = tokenEntity.getExpiresAt();

        if (createdAt == null) throw new BusinessException("Token createdAt date is null");

        if (confirmedAt != null) {
            if (createdAt.isAfter(confirmedAt))
                throw new BusinessException("token confirmed before creating");
            else
                throw new BusinessException("token already confirmed");
        }

        if (expiresAt == null) {
            throw new BusinessException("token expires is null");
        } else if (createdAt.isAfter(expiresAt)) {
            throw new BusinessException("token expired");
        }

        tokenEntity.setConfirmedAt(LocalDateTime.now());
        MailEntity mailEntity = mailService.getByEmail(tokenEntity.getEmail());
        mailEntity.setEnabled(true);
        mailService.update(mailEntity);
    }
}
