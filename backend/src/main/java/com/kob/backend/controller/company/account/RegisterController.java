package com.kob.backend.controller.company.account;

import com.kob.backend.service.company.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    @PostMapping("/api/company/account/register/")
    public Map<String, String> register(@RequestParam Map<String, String> map) {
        String companyname = map.get("companyname");
        String password = map.get("password");
        String confirmedPassword = map.get("confirmedPassword");
        String telephone = map.get("telephone");
        return registerService.register(companyname, password, confirmedPassword , telephone);
    }
}
