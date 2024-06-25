package com.kob.backend.controller.news.curd;

import com.kob.backend.service.news.curd.AddNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AddNewsController {
    @Autowired
    private AddNewsService addNewsService;

    @PostMapping("/user/add")
    public Map<String,String> addNews(@RequestParam Map<String , String> map){
        String title=map.get("title");
        String summary= map.get("summary");
        String imagePath = map.get("imagePath");
        String content = map.get("content");
        String author = map.get("author");
        String tenant = map.get("tenant");
        return addNewsService.addNews(title,summary,imagePath,content,author,tenant);
    }
}
