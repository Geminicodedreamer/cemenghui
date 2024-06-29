package com.kob.backend.service.impl.lesson.curd;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.LessonMapper;
import com.kob.backend.pojo.Lesson;
import com.kob.backend.service.lesson.curd.DeleteLessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteLessonServiceImp implements DeleteLessonService {
    @Autowired
    private LessonMapper lessonMapper;

    @Override
    public JSONObject deletelesson(Integer id) {
        QueryWrapper<Lesson> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id" , id);
        lessonMapper.delete(queryWrapper);
        JSONObject resp = new JSONObject();
        resp.put("error_message" , "success");
        return resp;
    }
}
