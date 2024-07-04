package com.kob.backend.controller.meeting.curd;

import com.kob.backend.service.meeting.curd.ModifyMeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ModifyMeetingController {
    @Autowired
    private ModifyMeetingService modifyMeetingService;

    @PostMapping("/api/meeting/modify")
    public Map<String, String> modifymeeting(@RequestParam Map<String, String> map)
    {
        Integer meetingid = Integer.valueOf(map.get("id"));
        String meetingname = map.get("name");
        String creator = map.get("creator");
        String content = map.get("content");
        String starttime = map.get("startTime");
        String endtime = map.get("endTime");
        String photo = map.get("photo");
        String companyname = map.get("companyname");
        return modifyMeetingService.modifymeeting(meetingid,meetingname,creator,content,starttime,endtime,photo , companyname);
    }
}
