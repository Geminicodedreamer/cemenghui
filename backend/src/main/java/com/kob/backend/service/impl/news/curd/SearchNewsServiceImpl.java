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

        if (title != null && !title.isEmpty()) {
            queryWrapper.like("title", title);
        }
        if (imagePath != null && !imagePath.isEmpty()) {
            queryWrapper.like("image_path", imagePath);
        }
        if (newsid != null) {
            queryWrapper.eq("newsid", newsid);
        }
        if (author != null && !author.isEmpty()) {
            queryWrapper.like("author", author);
        }
        if (summary != null && !summary.isEmpty()) {
            queryWrapper.like("summary", summary);
        }

        queryWrapper.orderByAsc("newsid");
        List<News> newsList = newsMapper.selectPage(newsIpage,queryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        List<JSONObject> items = new LinkedList<>();

        for(News news:newsList){
            JSONObject item = new JSONObject();
            item.put("newsid" , news.getNewsid());
            item.put("title",news.getTitle());
            item.put("summary",news.getSummary());
            item.put("imagePath",news.getImagePath());
            item.put("content",news.getContent());
            item.put("author",news.getAuthor());
            item.put("tenant",news.getTenant());
            items.add(item);
        }
        resp.put("news",items);
        resp.put("news_count",newsMapper.selectCount(queryWrapper));

        return resp;
    }
}
