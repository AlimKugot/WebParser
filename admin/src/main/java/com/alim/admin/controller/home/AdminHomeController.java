package com.alim.admin.controller.home;


import com.alim.admin.util.AdminUrl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminHomeController {

    @GetMapping(AdminUrl.ADMIN_HOME)
    public String getAdminHomePage() {
        return "/home/admin_home";
    }
}
