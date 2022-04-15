package com.alim.admin.model;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

import static com.alim.admin.model.Permission.*;

@Getter
@AllArgsConstructor
public enum Role {

    GUEST(Sets.newHashSet(
        PRODUCTS_READ,
        CATEGORIES_READ,
        PARSING_MODE_READ
    )),

    ADMIN(Sets.newHashSet(
        EMAILS_READ,
        EMAILS_WRITE,
        PRODUCTS_READ,
        PRODUCTS_WRITE,
        CATEGORIES_READ,
        CATEGORIES_WRITE,
        PARSING_MODE_READ,
        PARSING_MODE_WRITE,
        LOGS_READ,
        LOGS_WRITE
    ));

    private final Set<Permission> permissions;
}
