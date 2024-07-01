package com.kob.backend.controller.user.curd;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.user.curd.DeleteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteUserController {
    @Autowired
    private DeleteUserService deleteUserService;
    @GetMapping("/user/del")
    public JSONObject deleteuser(@RequestParam("id")String Id)
    {
        Integer id = Integer.parseInt(Id);
        return deleteUserService.deleteuser(id);
    }
}
