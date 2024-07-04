package com.kob.backend.controller.company.curd;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.company.curd.DeleteCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteCompanyController {
    @Autowired
    private DeleteCompanyService deleteCompanyService;

    @GetMapping("/api/company/del")
    public JSONObject deletecompany(@RequestParam("id")String Id)
    {
        Integer id = Integer.parseInt(Id);
        return deleteCompanyService.deletecompany(id);
    }
}
