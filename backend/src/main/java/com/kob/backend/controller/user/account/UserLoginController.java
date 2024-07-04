package com.kob.backend.controller.user.account;

import com.kob.backend.context.UserContext;
import com.kob.backend.service.user.account.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user/account")
public class UserLoginController {

    @Autowired
    private UserLoginService userLoginService;

    @PostMapping("/token")
    public Map<String, String> getToken(@RequestParam Map<String, String> map) {
        String userType = "user"; // 设置用户类型为普通用户
        UserContext.setUserType(userType);
        try {
            String username = map.get("username");
            String password = map.get("password");
            return userLoginService.getToken(username, password);
        }finally{
            UserContext.clear();
        }
    }
}
