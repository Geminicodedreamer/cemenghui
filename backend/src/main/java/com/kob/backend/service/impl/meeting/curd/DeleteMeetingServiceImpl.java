package com.kob.backend.service.impl.meeting.curd;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.MeetingMapper;
import com.kob.backend.pojo.Meeting;
import com.kob.backend.service.meeting.curd.DeleteMeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteMeetingServiceImpl implements DeleteMeetingService {
    @Autowired
    private MeetingMapper meetingMapper;

    @Override
    public JSONObject deletemeeting(Integer meetingid) {
        QueryWrapper<Meeting> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("meetingid",meetingid);
        meetingMapper.delete(queryWrapper);
        JSONObject resp = new JSONObject();
        resp.put("error_message","success");
        return resp;
    }
}
