package com.alim.mailsender.service.impl;

import com.alim.mailsender.dto.request.MailRequestDto;
import com.alim.mailsender.exception.BusinessException;
import com.alim.mailsender.factory.UuidFactory;
import com.alim.mailsender.model.MailEntity;
import com.alim.mailsender.repository.MailRepository;
import com.alim.mailsender.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final UuidFactory uuidFactory;
    private final MailRepository mailRepository;

    @Override
    public List<MailEntity> getAll() {
        return mailRepository.findAll();
    }

    @Override
    public MailEntity save(MailRequestDto mailRequestDto) {
        if (mailRequestDto == null) throw new BusinessException("Request is null");
        if (mailRepository.existsByEmail(mailRequestDto.getEmail()))
            throw new BusinessException("Email already exists");

        MailEntity user = MailEntity.builder()
                .id(uuidFactory.uuid())
                .email(mailRequestDto.getEmail())
                .isEnabled(false)
                .build();
        mailRepository.save(user);
        return user;
    }


    @Override
    public MailEntity getByEmail(String email) {
        return mailRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Email not exists"));
    }

    @Override
    @Transactional
    public void update(MailEntity mailEntity) {
        if (mailEntity == null) throw new BusinessException("Passed mailEntity is null");
        if (mailEntity.getEmail() == null || mailEntity.getEmail().isEmpty())
            throw new BusinessException("Email is empty or null");
        if (!mailRepository.existsById(mailEntity.getId()))
            throw new BusinessException("Email with id not found: " + mailEntity.getId());


        mailRepository.deleteById(mailEntity.getId());
        mailRepository.save(mailEntity);
    }
}
