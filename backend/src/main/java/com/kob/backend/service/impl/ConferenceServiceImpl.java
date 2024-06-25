package com.kob.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.backend.mapper.ConferenceMapper;
import com.kob.backend.pojo.Conference;
import com.kob.backend.service.ConferenceService;
import org.springframework.stereotype.Service;

@Service
public class ConferenceServiceImpl extends ServiceImpl<ConferenceMapper, Conference> implements ConferenceService {
}
