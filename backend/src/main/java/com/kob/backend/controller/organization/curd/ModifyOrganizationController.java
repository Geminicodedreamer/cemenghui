package com.kob.backend.controller.organization.curd;

import com.kob.backend.service.organization.curd.ModifyOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ModifyOrganizationController {
    @Autowired
    private ModifyOrganizationService modifyOrganizationService;
    @PostMapping("/api/organization/modify")
    private Map<String ,String > modifyorganization(@RequestParam Map<String ,String > map)
    {
        String organizationname =map.get("organizationname");
        String uporganization =map.get("uporganization");
        Integer id= Integer.parseInt(map.get("id"));
        String charger=map.get("charger");
        String telephone=map.get("telephone");
        String email =map.get("email");
        String status =map.get("status");

        return modifyOrganizationService.modifyorganization(id,uporganization,organizationname,charger,telephone,email,status);
    }
}
