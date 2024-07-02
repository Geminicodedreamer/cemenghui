package com.kob.backend.controller.user.curd;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.user.curd.GetUserListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetUserListController {
    @Autowired
    private GetUserListService getUserListService;

    @GetMapping("/user/list")
    public JSONObject getList(@RequestParam Map<String , String> map)
    {
        return getUserListService.getuserlist();
    }
}
