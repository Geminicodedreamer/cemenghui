package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserLoginController {
    @Autowired
    private UserLoginService userLoginService;

    @PostMapping("/user/account/token")
    public Map<String, String> getToken(@RequestParam Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        return userLoginService.getToken(username, password);
    }
}
