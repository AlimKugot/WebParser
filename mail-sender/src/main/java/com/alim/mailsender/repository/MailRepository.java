package com.alim.mailsender.repository;

import com.alim.mailsender.model.MailEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MailRepository extends MongoRepository<MailEntity, String> {

    boolean existsByEmail(String email);

    Optional<MailEntity> findByEmail(String email);

    void deleteByEmail(String email);
}
