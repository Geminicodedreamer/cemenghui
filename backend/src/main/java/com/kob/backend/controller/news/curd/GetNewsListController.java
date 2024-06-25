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

    @GetMapping("/news/list")
    public JSONObject getList(){
        return getNewsListService.getnewslist();
    }
}
