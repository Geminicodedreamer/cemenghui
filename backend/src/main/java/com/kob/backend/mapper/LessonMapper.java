package com.kob.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.pojo.Lesson;
import org.apache.ibatis.annotations.Mapper;

import java.util.stream.BaseStream;

@Mapper
public interface LessonMapper extends BaseMapper<Lesson> {
}
