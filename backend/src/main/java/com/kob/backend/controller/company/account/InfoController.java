package com.kob.backend.controller.company.account;

import com.kob.backend.service.company.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class InfoController {
    @Autowired
    private InfoService infoService;

    @GetMapping("/api/company/account/info/")
    public Map<String, String> getinfo() {
        return infoService.getinfo();
    }
}
