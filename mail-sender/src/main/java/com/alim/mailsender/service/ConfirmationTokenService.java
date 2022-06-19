package com.alim.mailsender.service;

import com.alim.mailsender.model.ConfirmationTokenEntity;

public interface ConfirmationTokenService {
    ConfirmationTokenEntity getNewToken(String email);

    void saveToken(ConfirmationTokenEntity confirmationToken);

    void confirmToken(String token);
}
