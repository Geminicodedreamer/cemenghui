package com.kob.backend.service.impl.news.curd;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.NewsMapper;
import com.kob.backend.pojo.News;
import com.kob.backend.service.news.curd.GetNewsListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class GetNewsListServiceImpl implements GetNewsListService {
    @Autowired
    private NewsMapper newsMapper;
    @Override
    public JSONObject getnewslist() {
        //IPage<News> newsIPage = new Page<>(page,10);
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("newsId");
        List<News> newsList = newsMapper.selectList(queryWrapper);
        JSONObject resp = new JSONObject();
        List<JSONObject> items = new LinkedList<>();

        for(News news : newsList){
            JSONObject item = new JSONObject();
            item.put("NewsId", news.getNewsid());
            item.put("title", news.getTitle());
            item.put("summary", news.getSummary());
            item.put("imagePath", news.getImagePath());
            item.put("content", news.getContent());
            item.put("Author" , news.getAuthor());//前端需要什么这里就装什么
            item.put("tenant", news.getTenant());
            items.add(item);
        }
        resp.put("news",items);
        //resp.put("news_count",newsMapper.selectCount(null));//分页，参数是“查询条件”

        return resp;
    }
}
