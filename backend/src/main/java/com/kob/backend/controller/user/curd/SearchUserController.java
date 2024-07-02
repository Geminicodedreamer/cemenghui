package com.kob.backend.controller.user.curd;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.user.curd.SearchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SearchUserController {
    @Autowired
    private SearchUserService searchUserService;

    @GetMapping("/user/search")
    public JSONObject searchuser(@RequestParam("page")Integer page,@RequestParam Map<String , String> map)
    {
        System.err.println(map);
        String companyname = map.get("companyname");
        String username= map.get("username");
        String telephone = map.get("telephone");
        Integer status = null;
        if(map.get("status") != null && !map.get("status").isEmpty()) {
            status = Integer.parseInt(map.get("status"));
        }
        String starttime = map.get("starttime");
        String endtime = map.get("endtime");
        return  searchUserService.searchuserlist(page , companyname , username , telephone , status , starttime , endtime);
    }
}
