package com.kob.backend.controller.organization.curd;


import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.organization.curd.SearchOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.beans.IntrospectionException;
import java.util.Map;

@RestController
public class SearchOrganizationController {
    @Autowired
    private SearchOrganizationService searchOrganizationService;

    @GetMapping("/organization/search")
    public JSONObject searchorganization(@RequestParam Map<String,String> map)
    {
        Integer page =Integer.parseInt(map.get("page"));
        String organizationname = map.get("organizationname");
        String status =map.get("status");
        return searchOrganizationService.searchorganization(page,organizationname,status);
    }
}
