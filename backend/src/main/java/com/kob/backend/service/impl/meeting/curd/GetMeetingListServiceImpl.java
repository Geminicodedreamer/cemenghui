package com.kob.backend.service.impl.meeting.curd;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.MeetingMapper;
import com.kob.backend.pojo.Meeting;
import com.kob.backend.service.meeting.curd.GetMeetingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class GetMeetingListServiceImpl implements GetMeetingListService {
    @Autowired
    private MeetingMapper meetingMapper;

    @Override
    public JSONObject getmeetinglist() {
        QueryWrapper<Meeting> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("meetingid");
        List<Meeting> meetingList = meetingMapper.selectList(queryWrapper);
        JSONObject resp = new JSONObject();
        List<JSONObject> items = new LinkedList<>();
        for(Meeting meeting : meetingList)
        {
            JSONObject item = new JSONObject();
            item.put("meetingid",meeting.getMeetingid());
            item.put("meetingname",meeting.getMeetingname());
            item.put("creator",meeting.getCreator());
            item.put("content",meeting.getContent());
            item.put("starttime",meeting.getStarttime());
            item.put("endtime",meeting.getEndtime());
            item.put("photo",meeting.getPhoto());
            items.add(item);
        }
        resp.put("meetings",items);

        return resp;
    }
}
