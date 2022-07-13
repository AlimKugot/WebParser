package com.alim.parser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ParserApplicationTests {

    @Autowired
    private ParserApplication app;

    @Test
    void contextLoads() {
        assertNotNull(app);
    }

}
