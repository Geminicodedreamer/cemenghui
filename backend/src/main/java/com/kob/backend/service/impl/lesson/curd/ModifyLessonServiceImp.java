package com.kob.backend.service.impl.lesson.curd;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.LessonMapper;
import com.kob.backend.pojo.Company;
import com.kob.backend.pojo.Lesson;
import com.kob.backend.service.lesson.curd.ModifyLessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModifyLessonServiceImp implements ModifyLessonService {
    @Autowired
    private LessonMapper lessonMapper;
    @Override
    public Map<String, String> modifylesson(Integer id, String lessonname, String lessonintro, String lessonauthor, String photo, String video , String companyname) {
        Map<String , String> map = new HashMap<>();
        if(id==null){
            map.put("error_message", "课程id不能为空");
            return map;
        }
        if(lessonname==null){
            map.put("error_message", "课程名称不能为空");
            return map;
        }
        if(lessonintro==null){
            map.put("error_message", "课程简介不能为空");
            return map;
        }
        if(lessonauthor==null){
            map.put("error_message", "课程作者不能为空");
            return map;
        }
        if(photo==null){
            map.put("error_message", "封面不能为空");
            return map;
        }
        if(video==null){
            map.put("error_message", "课程视频不能为空");
            return map;
        }
        QueryWrapper<Lesson> querywrapper_id = new QueryWrapper<>();
        querywrapper_id.eq("id" , id);
        Lesson lesson = lessonMapper.selectOne(querywrapper_id);
        QueryWrapper<Lesson> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lessonname", lessonname);
        List<Lesson> lessons = lessonMapper.selectList(queryWrapper);
        if (!lessons.isEmpty() && !lessonname.equals(lesson.getLessonname())) {
            map.put("error_message", "课程名称已存在");
            return map;
        }

        Lesson new_lesson = new Lesson(id, lessonname, lessonintro, lessonauthor,photo, video , companyname);
        lessonMapper.updateById(new_lesson);

        map.put("error_message" , "success");
        return map;
    }
}
