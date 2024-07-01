package com.kob.backend.controller.organization.curd;


import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.organization.curd.DeleteOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteOrganizationController {
    @Autowired
    private DeleteOrganizationService deleteOrganizationService;

    @GetMapping("/organization/del")
    public JSONObject deleteorganization(@RequestParam("id")String Id)
    {
        Integer id =Integer.parseInt(Id);
        return deleteOrganizationService.deleteorganition(id);
    }
}
