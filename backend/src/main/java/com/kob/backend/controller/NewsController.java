package com.kob.backend.controller;

import com.kob.backend.pojo.News;
import com.kob.backend.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @PostMapping("/add")
    public String addNews(@RequestBody News news) {
        newsService.save(news);
        return "News added successfully";
    }

    @GetMapping("/list")
    public List<News> listNews() {
        return newsService.list();
    }

    @PutMapping("/update")
    public String updateNews(@RequestBody News news) {
        newsService.updateById(news);
        return "News updated successfully";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteNews(@PathVariable Long id) {
        newsService.removeById(id);
        return "News deleted successfully";
    }
}
