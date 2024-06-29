package com.kob.backend.controller.lesson.curd;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.company.curd.GetCompanyListService;
import com.kob.backend.service.lesson.curd.GetLessonListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetLessonListController {
    @Autowired
    private GetLessonListService getLessonListService;
    @GetMapping("/lesson/list")
    public JSONObject getList()
    {
        return getLessonListService.getlessonlist();
    }
}
