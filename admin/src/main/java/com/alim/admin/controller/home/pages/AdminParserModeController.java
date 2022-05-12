package com.alim.admin.controller.home.pages;

import com.alim.admin.util.AdminUrl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminParserModeController {

    @GetMapping(AdminUrl.ADMIN_HOME_PARSER_MODE)
    public String getPage() {
        return "/home/pages/admin_parser_mode";
    }
}
