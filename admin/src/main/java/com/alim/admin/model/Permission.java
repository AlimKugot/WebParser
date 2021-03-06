package com.alim.admin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Permission {

    EMAILS_READ("emails:read"),
    EMAILS_WRITE("emails:write"),
    PRODUCTS_READ("products:read"),
    PRODUCTS_WRITE("products:write"),
    CATEGORIES_READ("categories:read"),
    CATEGORIES_WRITE("categories:write"),
    PARSER_MODE_READ("parsing:read"),
    PARSER_MODE_WRITE("parsing:write"),
    LOGS_READ("logs:read"),
    LOGS_WRITE("logs:write"),
    USERS_READ("users:read"),
    USERS_WRITE("users:write");

    private final String permission;
}
