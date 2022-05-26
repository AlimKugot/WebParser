package com.alim.mailsender;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootApplication
@RequiredArgsConstructor
public class MailSenderApplication implements CommandLineRunner {

    private final JavaMailSender javaMailSender;

    public static void main(String[] args) {
        SpringApplication.run(MailSenderApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("Sending Email...");
        sendEmail();
        System.out.println("Done");
    }


    void sendEmail() {
        SimpleMailMessage sms = new SimpleMailMessage();
        sms.setTo("alim.filipov@gmail.com");
        sms.setSubject("Я приду похаваю");

        javaMailSender.send(sms);
    }
}
