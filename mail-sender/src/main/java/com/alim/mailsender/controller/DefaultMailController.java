package com.alim.mailsender.controller;

import com.alim.mailsender.dto.request.MailRequestDto;
import com.alim.mailsender.model.ConfirmationTokenEntity;
import com.alim.mailsender.service.ConfirmationTokenService;
import com.alim.mailsender.service.MailSenderService;
import com.alim.mailsender.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.alim.mailsender.util.MailUrl.MAIL_CONFIRM;
import static com.alim.mailsender.util.MailUrl.MAIL_DEFAULT;


@RestController
@RequiredArgsConstructor
@RequestMapping(MAIL_DEFAULT)
public class DefaultMailController {

    private final MailService mailService;
    private final ConfirmationTokenService tokenService;
    private final MailSenderService mailSenderService;


    @GetMapping
    public String getAll() {
        return mailService.getAll().toString();
    }


    @PostMapping
    public String saveUser(@Validated @RequestBody MailRequestDto mailRequestDto) {
        ConfirmationTokenEntity confirmationToken = tokenService.getNewToken(mailRequestDto.getEmail());
        tokenService.saveToken(confirmationToken);

        mailSenderService.sendMessage(mailRequestDto.getEmail(), MAIL_CONFIRM + "?token=" + confirmationToken.getToken());
        mailService.save(mailRequestDto);
        return confirmationToken.toString();
    }
}

