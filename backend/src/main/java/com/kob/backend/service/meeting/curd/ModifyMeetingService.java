package com.kob.backend.service.meeting.curd;

import java.util.Map;

public interface ModifyMeetingService {
    public Map<String , String> modifymeeting(Integer meetingid, String meetingname, String creator, String content, String starttime, String endtime, String photo);
}
