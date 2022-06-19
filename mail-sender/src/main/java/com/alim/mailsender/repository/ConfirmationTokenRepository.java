package com.alim.mailsender.repository;

import com.alim.mailsender.model.ConfirmationTokenEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository
        extends MongoRepository<ConfirmationTokenEntity, String> {

    Optional<ConfirmationTokenEntity> findByToken(String token);
}
