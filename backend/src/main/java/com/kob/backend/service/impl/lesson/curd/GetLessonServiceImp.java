package com.kob.backend.service.impl.lesson.curd;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.LessonMapper;
import com.kob.backend.pojo.Company;
import com.kob.backend.pojo.Lesson;
import com.kob.backend.service.lesson.curd.GetLessonListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class GetLessonServiceImp implements GetLessonListService {
    @Autowired
    private LessonMapper lessonMapper;
    @Override
    public JSONObject getlessonlist() {
        QueryWrapper<Lesson> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        List<Lesson> lessonList = lessonMapper.selectList(queryWrapper);
        JSONObject resp = new JSONObject();
        List<JSONObject> items = new LinkedList<>();
        for (Lesson lesson: lessonList) {
            JSONObject item = new JSONObject();
            item.put("id" , lesson.getId());
            item.put("lessonname" , lesson.getLessonname());
            item.put("lessonintro" , lesson.getLessonintro());
            item.put("lessonauthor" , lesson.getLessonauthor());
            item.put("photo" , lesson.getPhoto());
            item.put("video" , lesson.getVideo());
            items.add(item);
        }
        resp.put("lessons", items);


        return resp;
    }
}
