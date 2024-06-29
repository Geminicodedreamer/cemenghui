package com.kob.backend.controller.meeting.curd;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.meeting.curd.DeleteMeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteMeetingController {
    @Autowired
    DeleteMeetingService deleteMeetingService;

    @GetMapping("/meeting/del")
    public JSONObject deletemeeting(@RequestParam("meetingid")String meetingId)
    {
        Integer meetingid = Integer.parseInt(meetingId);
        return deleteMeetingService.deletemeeting(meetingid);
    }
}
