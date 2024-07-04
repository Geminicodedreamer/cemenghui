package com.kob.backend.controller.meeting.curd;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.meeting.curd.SearchMeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SearchMeetingController {
    @Autowired
    private SearchMeetingService searchMeetingService;

    @GetMapping("/api/meeting/search")
    public JSONObject searchmeeting(@RequestParam Map<String, String> map)
    {
        Integer page = Integer.parseInt(map.get("page"));
        String meetingname = map.get("meetingname");
        String creator = map.get("creator");
        String starttime = map.get("starttime");
        String endtime = map.get("endtime");
        return searchMeetingService.searchmeeting(page,meetingname,creator,starttime);
    }
}
