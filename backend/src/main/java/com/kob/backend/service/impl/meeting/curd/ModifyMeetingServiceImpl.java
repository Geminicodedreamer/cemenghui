package com.kob.backend.service.impl.meeting.curd;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.MeetingMapper;
import com.kob.backend.pojo.Meeting;
import com.kob.backend.service.meeting.curd.ModifyMeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModifyMeetingServiceImpl implements ModifyMeetingService {
    @Autowired
    private MeetingMapper meetingMapper;


    @Override
    public Map<String, String> modifymeeting(Integer meetingid, String meetingname, String creator, String content, String starttime, String endtime, String photo, String companyname) {
        //System.err.println(meetingid+meetingname+creator+content+starttime+endtime+photo);
        Map<String, String> map = new HashMap<>();
        if(meetingname == null){
            map.put("error_message","会议名称不能为空");
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
        QueryWrapper<Meeting> querywrapper_id = new QueryWrapper<>();
        querywrapper_id.eq("meetingid",meetingid);
        Meeting meeting = meetingMapper.selectOne(querywrapper_id);
        QueryWrapper<Meeting> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("meetingname",meetingname);
        List<Meeting> meetings = meetingMapper.selectList(queryWrapper);
        if(!meetings.isEmpty() && !meetingname.equals(meeting.getMeetingname())){
            map.put("error_message","会议名称已存在");
            return map;
        }


        Meeting new_meeting = new Meeting(meetingid,meetingname,creator,content,starttime,endtime,photo,companyname);
        meetingMapper.updateById(new_meeting);

        map.put("error_message","success");
        return map;
    }
}
