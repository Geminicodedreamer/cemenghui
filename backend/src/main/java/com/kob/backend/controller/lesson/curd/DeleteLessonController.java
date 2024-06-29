package com.kob.backend.controller.lesson.curd;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.lesson.curd.DeleteLessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteLessonController {
    @Autowired
    private DeleteLessonService deleteLessonService;
    @GetMapping("/lesson/del")
    public JSONObject deletelesson(@RequestParam("id")String Id){
        Integer id=Integer.parseInt(Id);
        return deleteLessonService.deletelesson(id);
    }
}
