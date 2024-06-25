package com.kob.backend.service.news.curd;

import com.alibaba.fastjson.JSONObject;

public interface SearchNewsService {
    public JSONObject searchnews(Integer page,String title,String imagePath,Integer newsid,String author,String summary);
}
