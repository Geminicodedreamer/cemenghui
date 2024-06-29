package com.kob.backend.service.meeting.curd;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface AddMeetingService {
    public Map<String, String> addMeeting(String meetingname, String creator, String content, String starttime, String endtime, String photo);
}
