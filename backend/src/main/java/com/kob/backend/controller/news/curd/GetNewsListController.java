package com.kob.backend.controller.news.curd;


import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.news.curd.GetNewsListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetNewsListController {
    @Autowired
    private GetNewsListService getNewsListService;

    @GetMapping("/api/news/list")
    public JSONObject getList(){
//        JSONObject ob=getNewsListService.getnewslist();
//        System.out.println("ob:"+ob);
        return getNewsListService.getnewslist();
    }
}
