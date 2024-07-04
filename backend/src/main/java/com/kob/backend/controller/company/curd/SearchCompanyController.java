package com.kob.backend.controller.company.curd;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.company.curd.SearchCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SearchCompanyController {
    @Autowired
    private SearchCompanyService searchCompanyService;

    @GetMapping("/api/company/search")
    public JSONObject searchcompany(@RequestParam Map<String , String> map)
    {
        Integer page = Integer.parseInt(map.get("page"));
        String symbol = map.get("symbol");
        String owername = map.get("ownername");
        String telephone = map.get("telephone");
        String companyname = map.get("companyname");
        return searchCompanyService.searchcompany(page , symbol , owername , telephone , companyname);
    }
}
