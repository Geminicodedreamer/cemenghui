package com.kob.backend.controller.company.account;

import com.kob.backend.service.company.account.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/company/account/token/")
    public Map<String, String> getToken(@RequestParam Map<String, String> map) {
        String companyname = map.get("companyname");
        String password = map.get("password");
        return loginService.getToken(companyname, password);
    }
}
