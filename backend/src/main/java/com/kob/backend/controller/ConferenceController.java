package com.kob.backend.controller;

import com.kob.backend.pojo.Conference;
import com.kob.backend.service.ConferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conference")
public class ConferenceController {

    @Autowired
    private ConferenceService conferenceService;

    @PostMapping("/add")
    public String addConference(@RequestBody Conference conference) {
        conferenceService.save(conference);
        return "Conference added successfully";
    }

    @GetMapping("/list")
    public List<Conference> listConferences() {
        return conferenceService.list();
    }

    @PutMapping("/update")
    public String updateConference(@RequestBody Conference conference) {
        conferenceService.updateById(conference);
        return "Conference updated successfully";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteConference(@PathVariable Long id) {
        conferenceService.removeById(id);
        return "Conference deleted successfully";
    }
}
