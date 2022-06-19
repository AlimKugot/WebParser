package com.alim.mailsender.factory;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidFactory {


    public String uuid() {
        return UUID.randomUUID().toString();
    }
}
