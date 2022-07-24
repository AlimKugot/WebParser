package com.alim.admin;

import com.alim.admin.controller.AdminDefaultController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AdminApplicationTests {

    @Autowired
    private AdminDefaultController controller;

    @Test
    void contextLoads() {
        assertNotNull(controller);
    }

}
