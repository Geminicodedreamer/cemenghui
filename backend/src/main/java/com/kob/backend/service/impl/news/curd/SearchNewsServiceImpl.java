package com.kob.backend.service.impl.news.curd;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.NewsMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kob.backend.pojo.News;
import com.kob.backend.service.news.curd.SearchNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class SearchNewsServiceImpl implements SearchNewsService {
    @Autowired
    private NewsMapper newsMapper;
    @Override
    public JSONObject searchnews(Integer page, String title, String imagePath, Integer newsid, String author, String summary) {
        IPage<News> newsIpage = new Page<>(page,10);
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();

        //这里可以写后端的双重验证

        queryWrapper.orderByAsc("newsid");
        List<News> newsList = newsMapper.selectPage(newsIpage,queryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        List<JSONObject> items = new LinkedList<>();

        for(News news:newsList){
            JSONObject item = new JSONObject();
            item.put("title",news.getNewsid());
            item.put("summary",news.getSummary());
            item.put("imagePath",news.getImagePath());
            item.put("content",news.getContent());
            item.put("author",news.getAuthor());
            item.put("tenant",news.getTenant());
        }
        resp.put("news",items);
        resp.put("news_count",newsMapper.selectCount(queryWrapper));

        return resp;
    }
}
