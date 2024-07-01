package com.kob.backend.controller.user.curd;


import com.kob.backend.service.user.curd.ModifyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ModifyUserController {
    @Autowired
    private ModifyUserService modifyUserService;

    @PostMapping("/user/modify")
    public Map<String , String> modifyuser(@RequestParam Map<String , String> map)
    {
        Integer id = Integer.parseInt(map.get("userid"));
        String nickname = map.get("nickname");
        String telephone = map.get("telephone");
        String apartmentname = map.get("apartmentname");
        String gender = map.get("gender");
        Integer status = Integer.parseInt(map.get("status"));
        String email = map.get("email");
        String role = map.get("role");
        String note = map.get("note");
        String post = map.get("post");
        String photo = map.get("photo");
        return modifyUserService.modifyuser(id , nickname , telephone , apartmentname , gender , status , email , role , note , post , photo);
    }
}
