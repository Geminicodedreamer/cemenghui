package com.kob.backend.service.impl.news.curd;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.NewsMapper;
import com.kob.backend.pojo.News;
import com.kob.backend.service.news.curd.ModifyNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModifyNewsServiceImpl implements ModifyNewsService {
    @Autowired
    private NewsMapper newsMapper;
    @Override
    public Map<String, String> modifynews(Integer newsid,String title, String summary, String imagePath, String content, String author, String tenant) {
        Map<String,String> map = new HashMap<>();

        //这里可以实现后端的双重验证

        QueryWrapper<News> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("title",title);
        List<News> newslist=newsMapper.selectList(queryWrapper);
        if (!newslist.isEmpty()){
            map.put("error_message", "该新闻已存在");
            return map;
        }

        QueryWrapper<News> queryWrapper_id = new QueryWrapper<>();
        queryWrapper_id.eq("newsid",newsid);
        News news = newsMapper.selectOne(queryWrapper);
        News new_news=new News(newsid,title,summary,imagePath,content,author,tenant);
        newsMapper.updateById(new_news);

        map.put("error_message" , "success");
        return map;
    }
}
