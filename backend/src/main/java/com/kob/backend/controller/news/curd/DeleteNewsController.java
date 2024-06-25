package com.kob.backend.controller.news.curd;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.news.curd.DeleteNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteNewsController {
    @Autowired
    private DeleteNewsService deleteNewsService;

    @GetMapping("/news/del")
    public JSONObject deletenews(@RequestParam("newsid")String Id){
        Integer id = Integer.parseInt(Id);
        return deleteNewsService.deletenews(id);
    }
}