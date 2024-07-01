package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.ChangePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ChangePasswordController {
    @Autowired
    private ChangePasswordService changePasswordService;

    @PostMapping("/user/password")
    public Map<String , String> changepassword(@RequestParam Map<String , String> map)
    {
        Integer id = Integer.parseInt(map.get("id"));
        String password = map.get("password");
        String newpassword = map.get("newpassword");
        String confirmnewpassword = map.get("confirmnewpassword");
        return changePasswordService.changepassword(id , password , newpassword , confirmnewpassword);
    }
}