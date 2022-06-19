package com.alim.mailsender.service;

import com.alim.mailsender.service.impl.MailSenderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import javax.mail.internet.MimeMessage;

import static org.mockito.Mockito.*;

class MailSenderServiceTest {

    private static final String from = "alim-test@gmail.com";
    private JavaMailSender javaMailSender;
    private MailSenderService underTest;


    @BeforeEach
    void setup() {
        javaMailSender = mock(JavaMailSender.class);
        underTest = new MailSenderServiceImpl(javaMailSender);
        ReflectionTestUtils.setField(underTest, "fromEmail", from);
    }


    @Test
    void sendMessageSuccessfully() {
        MimeMessage mimeMessage = mock(MimeMessage.class);

        String to = "some@gmail.com";
        String link = "some-confirm-token-with-long-link";

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        underTest.sendMessage(to, link);
        verify(javaMailSender, times(1)).send(mimeMessage);
    }
}
