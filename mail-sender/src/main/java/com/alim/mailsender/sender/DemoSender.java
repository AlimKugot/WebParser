package com.alim.mailsender.sender;


import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@RequiredArgsConstructor
public class DemoSender {

    private final JavaMailSender javaMailSender;

    void sendEmail() {
        SimpleMailMessage sms = new SimpleMailMessage();
        sms.setTo("alim.filipov@gmail.com");
        sms.setSubject("Я приду похаваю");

        javaMailSender.send(sms);
    }
}
