package com.kob.backend.service.impl.news.curd;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.NewsMapper;
import com.kob.backend.pojo.News;
import com.kob.backend.service.news.curd.AddNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AddNewsServiceImpl implements AddNewsService {
    @Autowired
    private NewsMapper newsMapper;
    @Override
    public Map<String, String> addNews(String title, String summary, String imagePath, String content, String author, String tenant) {
        Map<String,String> map =new HashMap<>();

//        这里可以进行后端的二次验证

        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",title);
        List<News> news = newsMapper.selectList(queryWrapper);

        if(!news.isEmpty()){
            map.put("error_message","该新闻已存在");
            return map;
        }
        News news1= new News(null,title, summary, imagePath, content, author, tenant);
        newsMapper.insert(news1);

        map.put("error_message", "success");
        return map;
    }
}
