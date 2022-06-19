package com.alim.mailsender.factory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UuidFactoryTest {

    private static final int UUID_LENGTH = 36;
    private final UuidFactory underTest = new UuidFactory();

    @Test
    void uuid() {
        String uuid = underTest.uuid();

        assertNotNull(uuid);
        assertEquals(UUID_LENGTH, uuid.length());
    }
}
