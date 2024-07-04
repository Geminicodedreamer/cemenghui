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

    @PostMapping("/api/user/add")
    public Map<String , String> addUser(@RequestParam Map<String , String> map)
    {
        String username = map.get("username");
        String nickname = map.get("nickname");
        String telephone = map.get("telephone");
        String apartmentname = map.get("apartmentname");
        String gender = map.get("gender");
        Integer status = Integer.parseInt(map.get("status"));
        String email = map.get("email");
        String role = map.get("role");
        String note = map.get("note");
        String companyname = map.get("companyname");
        String post = map.get("post");
        String createtime = map.get("createtime");
        String photo = map.get("photo");
        String password = map.get("password");
        return addUserService.addUser(username , nickname , password , telephone , apartmentname , gender , status , email , role , note , companyname , post ,photo, createtime);
    }
}
