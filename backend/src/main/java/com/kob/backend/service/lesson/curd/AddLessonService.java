package com.kob.backend.service.lesson.curd;

import java.util.Map;

public interface AddLessonService {
    public Map<String , String> addLesson(Integer id, String lessonname, String lessonintro, String lesonauthor, String photo, String video);
}
