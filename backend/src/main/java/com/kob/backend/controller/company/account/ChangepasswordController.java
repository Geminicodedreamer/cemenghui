package com.kob.backend.controller.company.account;

import com.kob.backend.service.company.account.ChangepasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ChangepasswordController {
    @Autowired
    private ChangepasswordService changepasswordService;

    @PostMapping("/company/password")
    public Map<String , String> changepassword(@RequestParam Map<String , String> map)
    {
        Integer id = Integer.parseInt(map.get("id"));
        String password = map.get("password");
        String newpassword = map.get("newpassword");
        String confirmnewpassword = map.get("confirmnewpassword");
        return changepasswordService.changepassword(id , password , newpassword , confirmnewpassword);
    }
}
