package com.alim.admin.controller;

import com.alim.admin.model.AdminEntity;
import com.alim.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/sign_up")
public class AdminSignupController {

    private final AdminService adminService;

    @GetMapping
    public String getSignupPage() {
        return "admin_signup";
    }


    @ResponseBody
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String signUp(@RequestParam MultiValueMap<String, String> body) {
        AdminEntity admin = adminService.save(body);
        return admin.toString();
    }
}
