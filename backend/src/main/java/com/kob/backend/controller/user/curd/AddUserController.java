package com.kob.backend.controller.user.curd;

import com.kob.backend.service.user.curd.AddUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AddUserController {
    @Autowired
    private AddUserService addUserService;

    @PostMapping("/user/add")
    public Map<String , String> addUser(@RequestParam Map<String , String> map)
    {
        String username = map.get("username");
        String photo = map.get("photo");
        String password = map.get("password");
        return addUserService.addUser(username , photo , password);
    }
}
