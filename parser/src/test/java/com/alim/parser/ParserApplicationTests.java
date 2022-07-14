package com.alim.parser;

import com.alim.parser.controller.CategoriesController;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Tag("integration")
class ParserApplicationTests {

    @Autowired
    private CategoriesController bean;

    @Test()
    void contextLoads() {
        assertNotNull(bean);
    }

}
