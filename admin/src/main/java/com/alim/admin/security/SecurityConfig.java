package com.alim.admin.security;

import com.alim.admin.model.Role;
import com.alim.admin.util.AdminUrl;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static com.alim.admin.model.Permission.*;
import static com.alim.admin.util.AdminUrl.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .anonymous()
                    .principal(Role.GUEST.name())
                    .authorities("GUEST_ROLE")
                    .and()
                .authorizeRequests()
                    .antMatchers(AdminUrl.ADMIN_DEFAULT, ADMIN_HOME, "/css/*", "/js/**").permitAll()
                    .and()
                .authorizeRequests()
                    .antMatchers(ADMIN_HOME_CATEGORIES).hasAuthority(CATEGORIES_READ.getPermission())
                    .antMatchers(ADMIN_HOME_EMAILS).hasAuthority(EMAILS_READ.getPermission())
                    .antMatchers(ADMIN_HOME_LOGS).hasAuthority(LOGS_READ.getPermission())
                    .antMatchers(ADMIN_HOME_PARSER_MODE).hasAuthority(PARSER_MODE_WRITE.getPermission())
                    .antMatchers(ADMIN_HOME_PRODUCTS).hasAuthority(PRODUCTS_READ.getPermission())
                    .antMatchers(ADMIN_HOME_USERS).hasAuthority(USERS_READ.getPermission())
                    .anyRequest()
                    .permitAll()
                    .and()
                .formLogin()
                    .loginPage(AdminUrl.ADMIN_LOGIN)
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl(ADMIN_LOGOUT)
                    .logoutSuccessUrl(ADMIN_LOGIN)
                    .and()
                .httpBasic();
    }
}
