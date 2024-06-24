package com.kob.backend.controller.company.curd;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.company.curd.GetCompanyListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetCompanyListController {
    @Autowired
    private GetCompanyListService getCompanyListService;

    @GetMapping("/company/list")
    public JSONObject getList(@RequestParam Map<String ,String> data)
    {
        Integer page =  Integer.parseInt(data.get("page"));
        return getCompanyListService.getcompanylist(page);
    }
}
