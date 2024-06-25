package com.kob.backend.service.impl.news.curd;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.NewsMapper;
import com.kob.backend.pojo.News;
import com.kob.backend.service.news.curd.DeleteNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteNewsServiceImpl implements DeleteNewsService {
    @Autowired
    private NewsMapper newsMapper;
    @Override
    public JSONObject deletenews(Integer newsid) {
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("newsid",newsid);
        newsMapper.delete(queryWrapper);
        JSONObject resp = new JSONObject();
        resp.put("error_message" , "success");
        return resp;
    }
}
