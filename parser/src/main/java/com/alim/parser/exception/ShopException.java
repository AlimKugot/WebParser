package com.alim.parser.exception;

import lombok.NoArgsConstructor;

/**
 * Mein simple unchecked exception
 */
@NoArgsConstructor
public class ShopException extends RuntimeException {

    /**
     * Base constructor to hand errors
     * @param message cause of exception
     */
    public ShopException(String message) { super(message);}
}
