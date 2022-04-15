package com.alim.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders  .HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                    .antMatchers("/home").permitAll()
//                    .anyRequest().authenticated()
//                    .and()
//                .formLogin()
//                    .loginPage("/admin/login")
//                    .permitAll()
//                    .and()
//                .logout()
//                    .permitAll();

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }
}
