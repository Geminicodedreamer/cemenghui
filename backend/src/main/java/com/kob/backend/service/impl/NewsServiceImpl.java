package com.kob.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.backend.mapper.NewsMapper;
import com.kob.backend.pojo.News;
import com.kob.backend.service.NewsService;
import org.springframework.stereotype.Service;

@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {
}
