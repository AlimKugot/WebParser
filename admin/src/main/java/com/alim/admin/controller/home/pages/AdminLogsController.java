package com.alim.admin.controller.home.pages;

import com.alim.admin.util.AdminUrl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminLogsController {

    @GetMapping(AdminUrl.ADMIN_HOME_LOGS)
    public String getPage() {
        return "/home/pages/admin_logs";
    }
}
