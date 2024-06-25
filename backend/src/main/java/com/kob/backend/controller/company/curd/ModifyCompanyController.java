package com.kob.backend.controller.company.curd;

import com.kob.backend.service.company.curd.ModifyCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class ModifyCompanyController {
    @Autowired
    private ModifyCompanyService modifyCompanyService;

    @PostMapping("/company/modify")
    public Map<String , String> modifycompany(@RequestParam Map<String , String> map)
    {
        Integer companyId = Integer.parseInt(map.get("companyid"));
        String companyname = map.get("companyname");
        String photo = map.get("photo");
        String ownername = map.get("ownername");
        String telephone = map.get("telephone");
        String note = map.get("note");
        return modifyCompanyService.modifycompany( companyId ,companyname, photo, ownername, telephone, note);
    }
}