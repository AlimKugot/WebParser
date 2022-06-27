package com.alim.mailsender.service;

import com.alim.mailsender.dto.request.MailRequestDto;
import com.alim.mailsender.model.MailEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MailService {
    List<MailEntity> getAll();

    MailEntity save(MailRequestDto user);

    MailEntity getByEmail(String email);

    @Transactional
    void update(MailEntity mailEntity);
}
