package com.alim.admin.controller;

import com.alim.admin.util.AdminUrl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AdminLoginController {

    @GetMapping(AdminUrl.ADMIN_LOGIN)
    public String getLoginPage() {
        return "admin_login";
    }
}
