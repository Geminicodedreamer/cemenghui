package com.kob.backend.service.impl.meeting.curd;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.MeetingMapper;
import com.kob.backend.pojo.Meeting;
import com.kob.backend.service.meeting.curd.AddMeetingService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AddMeetingServiceImpl implements AddMeetingService {

    @Autowired
    private MeetingMapper meetingMapper;

    @Override
    public Map<String, String> addMeeting(String meetingname, String creator, String content, String starttime, String endtime, String photo) {
        System.err.println(content.length());
        Map<String, String> map = new HashMap<>();
        if(meetingname == null){
            map.put("error_message","会议名不能为空");
            return map;
        }

        if(creator == null){
            map.put("error_message","创建人不能为空");
            return map;
        }

        if(content == null){
            map.put("error_message","会议内容不能为空");
            return map;
        }

        if(starttime == null){
            map.put("error_message","会议开始时间不能为空");
            return map;
        }

        if(endtime == null){
            map.put("error_message","会议结束时间不能为空");
            return map;
        }


        QueryWrapper<Meeting> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("meetingname",meetingname);
        List<Meeting> meetings = meetingMapper.selectList(queryWrapper);

        if (!meetings.isEmpty()) {
            map.put("error_message","会议已存在");
            return map;
        }

        Meeting meeting = new Meeting(null,meetingname,creator,content,starttime,endtime,photo);
        meetingMapper.insert(meeting);

        // 获取插入后的会议ID
        Integer meetingid = meeting.getMeetingid();

        map.put("error_message","success");
        map.put("meetingid", meetingid.toString());  // 返回会议ID
        return map;
    }
}