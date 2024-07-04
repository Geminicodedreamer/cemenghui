package com.kob.backend.controller.meeting.curd;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.meeting.curd.GetMeetingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetMeetingListController {
    @Autowired
    private GetMeetingListService getMeetingListService;

    @GetMapping("/api/meeting/list")
    public JSONObject getList() {
        return getMeetingListService.getmeetinglist();
    }
}
