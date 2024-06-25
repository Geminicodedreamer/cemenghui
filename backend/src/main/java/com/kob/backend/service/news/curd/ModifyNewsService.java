package com.kob.backend.service.news.curd;

import java.util.Map;

public interface ModifyNewsService {
    public Map<String ,String> modifynews(Integer newsid,String title,String summary,String imagePath,String content,String author,String tenant);
}
