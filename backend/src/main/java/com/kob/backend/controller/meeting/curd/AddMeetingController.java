package com.kob.backend.controller.meeting.curd;

import com.kob.backend.service.meeting.curd.AddMeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AddMeetingController {
    @Autowired
    private AddMeetingService addMeetingService;

    @PostMapping("/meeting/add/")
    public Map<String, String> addMeeting(@RequestParam Map<String, String> map) {
        String meetingname = map.get("meetingname");
        String creator = map.get("creator");
        String content = map.get("content");
        String starttime = map.get("starttime");
        String endtime = map.get("endtime");
        String photo = map.get("photo");
        return addMeetingService.addMeeting(meetingname, creator, content, starttime, endtime, photo);
    }

}
