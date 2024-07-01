package com.kob.backend.controller.organization.curd;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.organization.curd.GetOrganizationListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetOrganizationListController {
    @Autowired
    private GetOrganizationListService getOrganizationListService;

    @GetMapping("/organization/list")

    public JSONObject getList(){

        return getOrganizationListService.getorganizationlist();
    }

}
