package com.kob.backend.service.meeting.curd;

import com.alibaba.fastjson.JSONObject;

public interface DeleteMeetingService {
    public JSONObject deletemeeting(Integer meetingid);
}
