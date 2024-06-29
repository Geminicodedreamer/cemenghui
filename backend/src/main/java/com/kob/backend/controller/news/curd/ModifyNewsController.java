package com.kob.backend.controller.news.curd;

import com.kob.backend.service.news.curd.ModifyNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class ModifyNewsController {
    @Autowired
    private ModifyNewsService modifyNewsService;

    @PostMapping("/news/modify")
    public Map<String,String> modifynews(@RequestParam Map<String,String> map){
        Integer newsId=Integer.parseInt(map.get("newsid"));
        String title = map.get("title");
        String summary = map.get("summary");
        String imagePath = map.get("imagePath");
        String content = map.get("content");
        String author = map.get("author");
        String tenant = map.get("tenant");
        return modifyNewsService.modifynews(newsId,title,summary,imagePath,content,author,tenant);

    }
}
