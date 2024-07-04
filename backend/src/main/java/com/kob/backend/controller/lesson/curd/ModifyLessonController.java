package com.kob.backend.controller.lesson.curd;

import com.kob.backend.service.lesson.curd.ModifyLessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ModifyLessonController {
    @Autowired
    private ModifyLessonService modifyLessonService;

    @PostMapping("/api/lesson/modify")
    public Map<String,String> modifylesson(@RequestParam Map<String,String> map){
        Integer id=Integer.parseInt(map.get("id"));
        String  lessonname=map.get("lessonname");
        String lessonintro=map.get("lessonintro");
        String lessonauthor=map.get("lessonauthor");
        String photo=map.get("photo");
        String video=map.get("video");
        String companyname = map.get("companyname");
        return modifyLessonService.modifylesson(id,lessonname,lessonintro,lessonauthor,photo,video , companyname);
    }
}
