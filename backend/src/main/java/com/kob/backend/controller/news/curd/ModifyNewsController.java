package com.kob.backend.controller.news.curd;

import com.kob.backend.service.news.curd.ModifyNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ModifyNewsController {
    @Autowired
    private ModifyNewsService modifyNewsService;

    @PostMapping("/api/news/modify")
    public Map<String,String> modifynews(@RequestParam Map<String,String> map){
        System.out.println("Received parameters: " + map); // 调试输出
        Integer newsId=Integer.parseInt(map.get("newsid"));
        String title = map.get("title");
        String summary = map.get("summary");
        String imagePath = map.get("photo");
        String content = map.get("content");
        String author = map.get("author");
        String tenant = map.get("tenant");
        return modifyNewsService.modifynews(newsId,title,summary,imagePath,content,author,tenant);

        //以下是批量用于修改的代码
//        @PostMapping("/company/modifyadmin")
//        public Map<String, String> modifyAdmin(@RequestParam Integer companyid, @RequestParam String adminname) {
//            return modifyCompanyService.modifyAdmin(companyid, adminname);
    }
}
