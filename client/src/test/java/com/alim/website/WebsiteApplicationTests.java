package com.alim.website;

import com.alim.website.controller.HomeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class WebsiteApplicationTests {

    @Autowired
    private HomeController controller;

    @Test
    void contextLoads() {
        assertNotNull(controller);
    }

}
