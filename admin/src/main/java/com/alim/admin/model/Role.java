package com.alim.admin.model;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.alim.admin.model.Permission.CATEGORIES_READ;
import static com.alim.admin.model.Permission.CATEGORIES_WRITE;
import static com.alim.admin.model.Permission.EMAILS_READ;
import static com.alim.admin.model.Permission.EMAILS_WRITE;
import static com.alim.admin.model.Permission.LOGS_READ;
import static com.alim.admin.model.Permission.LOGS_WRITE;
import static com.alim.admin.model.Permission.PARSER_MODE_READ;
import static com.alim.admin.model.Permission.PARSER_MODE_WRITE;
import static com.alim.admin.model.Permission.PRODUCTS_READ;
import static com.alim.admin.model.Permission.PRODUCTS_WRITE;
import static com.alim.admin.model.Permission.USERS_READ;
import static com.alim.admin.model.Permission.USERS_WRITE;

@Getter
@AllArgsConstructor
public enum Role {

    GUEST("GUEST", Sets.newHashSet(
            PRODUCTS_READ,
            CATEGORIES_READ,
            PARSER_MODE_READ
    )),

    ADMIN("ADMIN", Sets.newHashSet(
            EMAILS_READ,
            EMAILS_WRITE,
            PRODUCTS_READ,
            PRODUCTS_WRITE,
            CATEGORIES_READ,
            CATEGORIES_WRITE,
            PARSER_MODE_READ,
            PARSER_MODE_WRITE,
            LOGS_READ,
            LOGS_WRITE
    )),

    SUPER_ADMIN("SUPER-ADMIN", Sets.newHashSet(
            EMAILS_READ,
            EMAILS_WRITE,
            PRODUCTS_READ,
            PRODUCTS_WRITE,
            CATEGORIES_READ,
            CATEGORIES_WRITE,
            PARSER_MODE_READ,
            PARSER_MODE_WRITE,
            LOGS_READ,
            LOGS_WRITE,
            USERS_READ,
            USERS_WRITE
    ));

    private final String roleName;
    private final Set<Permission> permissions;

    public Set<GrantedAuthority> getGrantedAuthorities() {
        Set<GrantedAuthority> authorities = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
