package com.kob.backend.service.news.curd;


import java.util.Map;

public interface AddNewsService {
    public Map<String , String> addNews(String title,String summary,String imagePath,String content,String author,String tenant);
}
