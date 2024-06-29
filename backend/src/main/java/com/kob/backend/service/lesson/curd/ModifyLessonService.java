package com.kob.backend.service.lesson.curd;

import java.util.Map;

public interface ModifyLessonService {
    public Map<String ,String> modifylesson(Integer id, String lessonname, String lessonintro, String lesonauthor, String photo, String video);
}
