package com.kob.backend.service.news.curd;

import com.alibaba.fastjson.JSONObject;

public interface DeleteNewsService {
    public JSONObject deletenews(Integer newsid);
}
