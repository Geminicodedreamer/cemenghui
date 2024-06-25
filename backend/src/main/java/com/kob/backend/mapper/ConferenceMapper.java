package com.kob.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.pojo.Conference;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConferenceMapper extends BaseMapper<Conference> {
}
