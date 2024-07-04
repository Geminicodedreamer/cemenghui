package com.kob.backend.controller.company.account;

import com.kob.backend.context.UserContext;
import com.kob.backend.service.company.account.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/company/account")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/token")
    public Map<String, String> getToken(@RequestParam Map<String, String> map) {
        String userType = "company"; // 设置用户类型为公司
        UserContext.setUserType(userType);
        try {
            String companyname = map.get("companyname");
            String password = map.get("password");
            return loginService.getToken(companyname, password);
        } finally {
            UserContext.clear();
        }
    }
}
