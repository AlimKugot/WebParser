package com.alim.admin.security;

import com.alim.admin.model.Role;
import com.alim.admin.util.AdminUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.alim.admin.model.Permission.CATEGORIES_READ;
import static com.alim.admin.model.Permission.EMAILS_READ;
import static com.alim.admin.model.Permission.LOGS_READ;
import static com.alim.admin.model.Permission.PARSER_MODE_WRITE;
import static com.alim.admin.model.Permission.PRODUCTS_READ;
import static com.alim.admin.model.Permission.USERS_READ;
import static com.alim.admin.util.AdminUrl.ADMIN_HOME;
import static com.alim.admin.util.AdminUrl.ADMIN_HOME_CATEGORIES;
import static com.alim.admin.util.AdminUrl.ADMIN_HOME_EMAILS;
import static com.alim.admin.util.AdminUrl.ADMIN_HOME_LOGS;
import static com.alim.admin.util.AdminUrl.ADMIN_HOME_PARSER_MODE;
import static com.alim.admin.util.AdminUrl.ADMIN_HOME_PRODUCTS;
import static com.alim.admin.util.AdminUrl.ADMIN_HOME_USERS;
import static com.alim.admin.util.AdminUrl.ADMIN_LOGIN;
import static com.alim.admin.util.AdminUrl.ADMIN_LOGOUT;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

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
                    .loginPage(ADMIN_LOGIN).permitAll()
                    .defaultSuccessUrl(ADMIN_HOME)
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .and()
                .rememberMe()
                    .key("remember-me")
                    .and()
                .logout()
                    .logoutUrl(ADMIN_LOGOUT)
                    .logoutSuccessUrl(ADMIN_LOGIN);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)  {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }
}
