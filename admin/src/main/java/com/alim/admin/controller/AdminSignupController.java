package com.alim.admin.controller;

import com.alim.admin.dto.request.UserRequestDto;
import com.alim.admin.service.UserService;
import com.alim.admin.util.AdminUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping(AdminUrl.ADMIN_SIGNUP)
public class AdminSignupController {

    private final UserService userService;

    @GetMapping
    public String getSignupPage() {
        return "admin_signup";
    }

    @PostMapping
    public String signUp(UserRequestDto userRequestDto) {
        userService.save(userRequestDto);
        return "redirect:/admin";
    }
}
