package com.alim.admin.security.auth;

import com.alim.admin.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

@AllArgsConstructor
@Getter
public class UserDetailsImpl implements UserDetails {

    private final Set<? extends GrantedAuthority> authorities;
    private final String username;
    private final String password;
    private final boolean isAccountNonExpired;
    private final boolean isAccountNonLocked;
    private final boolean isCredentialsNonExpired;
    private final boolean isEnabled;

    public UserDetailsImpl(UserEntity userEntity) {
        this.username = userEntity.getName();
        this.password = userEntity.getPassword();
        this.authorities = userEntity.getRole().getGrantedAuthorities();
        isAccountNonExpired = true;
        isAccountNonLocked = true;
        isCredentialsNonExpired = true;
        isEnabled = true;
    }
}
