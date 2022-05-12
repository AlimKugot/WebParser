package com.alim.admin.controller;

import com.alim.admin.dto.request.LoginRequestDto;
import com.alim.admin.util.AdminUrl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class AdminLoginController {

    @GetMapping(AdminUrl.ADMIN_LOGIN)
    public String getLoginPage() {
        return "admin_login";
    }

    @PostMapping(AdminUrl.ADMIN_LOGIN)
    public String postLogin(LoginRequestDto loginRequestDto) {
        System.out.println(loginRequestDto.getName());
        System.out.println(loginRequestDto.getPassword());
        return "redirect:/admin";
    }
}
