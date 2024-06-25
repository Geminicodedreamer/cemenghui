package com.kob.backend.controller.news.curd;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.news.curd.SearchNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SearchNewsController {
    @Autowired
    private SearchNewsService searchNewsService;

    @GetMapping("/news/search")
    public JSONObject searchnews(@RequestParam Map<String,String> map){
        Integer page = Integer.parseInt(map.get("page"));
        String title = map.get("title");
        String imagePath = map.get("imagePath");
        Integer newsid = Integer.valueOf(map.get("newsid"));
        String author = map.get("author");
        String summary = map.get("summary");
        return searchNewsService.searchnews(page,title,imagePath, newsid,author,summary);
    }
}
