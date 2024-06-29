package com.kob.backend.service.meeting.curd;

import com.alibaba.fastjson.JSONObject;

public interface SearchMeetingService {
    public JSONObject searchmeeting(Integer page, String meetingname , String creator, String starttime);
}
