package com.alim.admin.controller;

import com.alim.admin.util.AdminUrl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDefaultController {

    @GetMapping(AdminUrl.ADMIN_DEFAULT)
    public String getPage() {
        return "redirect:/admin/home";
    }
}
