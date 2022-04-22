package com.alim.admin.controller.home.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/users")
public class AdminUsersController {

    @GetMapping
    public String getPage() {
        return "/home/pages/admin_users";
    }
}
