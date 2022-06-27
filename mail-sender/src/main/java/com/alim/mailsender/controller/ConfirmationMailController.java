package com.alim.mailsender.controller;

import com.alim.mailsender.service.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.alim.mailsender.util.MailUrl.MAIL_CONFIRM;

@RestController
@RequiredArgsConstructor
public class ConfirmationMailController {

    private final ConfirmationTokenService tokenService;

    @GetMapping(MAIL_CONFIRM)
    public String confirmEmail(@RequestParam("token") String token) {
        tokenService.confirmToken(token);
        return "confirmed";
    }
}
