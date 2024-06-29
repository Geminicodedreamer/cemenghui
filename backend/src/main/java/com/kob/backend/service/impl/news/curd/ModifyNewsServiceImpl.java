package com.kob.backend.service.impl.news.curd;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.NewsMapper;
import com.kob.backend.pojo.Company;
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

        QueryWrapper<News> queryWrapper_id = new QueryWrapper<>();
        queryWrapper_id.eq("newsid",newsid);
        News news = newsMapper.selectOne(queryWrapper_id);
        QueryWrapper<News> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("title",title);
        List<News> newslist=newsMapper.selectList(queryWrapper);
        if (!newslist.isEmpty() && !title.equals(news.getTitle())){
            map.put("error_message", "该新闻已存在");
            return map;
        }

        News new_news=new News(newsid,title,summary,imagePath,content,author,tenant);
        newsMapper.updateById(new_news);

        map.put("error_message" , "success");
        return map;
    }

    //以下是实现批量修改的部分
//    @Override
//    public Map<String, String> modifyAdmin(Integer companyId, String adminname) {
//        Map<String, String> map = new HashMap<>();
//        if (adminname == null || adminname.trim().isEmpty()) {
//            map.put("error_message", "管理员名字不能为空");
//            return map;
//        }
//
//        Company company = companyMapper.selectById(companyId);
//        if (company == null) {
//            map.put("error_message", "公司不存在");
//            return map;
//        }
//
//        company.setAdminname(adminname);
//        companyMapper.updateById(company);
//
//        map.put("error_message", "success");
//        return map;
//    }
}
