package com.leti.webparser.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ShopException extends RuntimeException {

    public ShopException(String message) { super(message);}
}
