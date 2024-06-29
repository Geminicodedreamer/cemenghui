package com.kob.backend.service.impl.meeting.curd;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.MeetingMapper;
import com.kob.backend.pojo.Meeting;
import com.kob.backend.service.meeting.curd.SearchMeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class SearchMeetingServiceImpl implements SearchMeetingService {
    @Autowired
    private MeetingMapper meetingMapper;

    @Override
    public JSONObject searchmeeting(Integer page, String meetingname , String creator, String starttime) {
        IPage<Meeting> meetingIPage = new Page<>(page, 10);
        QueryWrapper<Meeting> queryWrapper = new QueryWrapper<>();
        if(meetingname != null && !meetingname.isEmpty()){
            queryWrapper.like("meetingname", meetingname);
        }

        if(creator != null && creator.isEmpty()){
            queryWrapper.like("creator",creator);
        }

        if(starttime != null && starttime.isEmpty()){
            queryWrapper.like("starttime",starttime);
        }

        queryWrapper.orderByAsc("meetingid");
        List<Meeting> meetingList = meetingMapper.selectPage(meetingIPage, queryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        List<JSONObject> items = new LinkedList<>();
        for(Meeting meeting : meetingList) {
            JSONObject item = new JSONObject();
            item.put("meetingid", meeting.getMeetingid());
            item.put("meetingname",meeting.getMeetingname());
            item.put("creator",meeting.getCreator());
            item.put("content",meeting.getContent());
            item.put("starttime",meeting.getStarttime());
            item.put("endtime",meeting.getEndtime());
            item.put("photo",meeting.getPhoto());
            items.add(item);
        }
        resp.put("meetings",items);
        resp.put("meeting_count",meetingMapper.selectCount(queryWrapper));

        return resp;
    }
}
