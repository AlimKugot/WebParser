package com.alim.mailsender;

import com.alim.mailsender.controller.DefaultMailController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class MailApplicationTests {

    @Autowired
    private DefaultMailController controller;


    @Test
    void contextLoads() {
        assertNotNull(controller);
    }
}
