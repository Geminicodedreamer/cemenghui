package com.kob.backend.controller.organization.curd;


import com.kob.backend.service.organization.curd.AddOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AddOrganizationController {
    @Autowired
    private AddOrganizationService addOrganizationService;

    @PostMapping("/organization/add")
    public Map<String, String> addOrganization(@RequestParam Map<String, String> map) {

            String uporganization = map.get("uporganization");
            String organizationname = map.get("organizationname");
            String charger = map.get("charger");
            String telephone = map.get("telephone");
            String email = map.get("email");
            String status = map.get("status");
            String creattime=map.get("creattime");

        return addOrganizationService.addOrganization(uporganization, organizationname, null, charger, telephone, email,status,creattime);
    }
}

