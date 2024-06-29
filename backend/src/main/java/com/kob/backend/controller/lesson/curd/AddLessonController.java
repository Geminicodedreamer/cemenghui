package com.kob.backend.controller.lesson.curd;

import com.kob.backend.service.lesson.curd.AddLessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AddLessonController {
    @Autowired
    private AddLessonService addLessonService;

    @PostMapping("/lesson/add")
    public Map<String,String> addLesson(@RequestParam Map<String , String> map){
        Integer id= Integer.valueOf(map.get("id"));
        String lessonnname=map.get("lessonname");
        String lessonintro=map.get("lessonintro");
        String lessonauthor=map.get("lessonauthor");
        String photo=map.get("photo");
        String video=map.get("video");
        return  addLessonService.addLesson(id,lessonnname,lessonintro,lessonauthor,photo,video);



    }
}
