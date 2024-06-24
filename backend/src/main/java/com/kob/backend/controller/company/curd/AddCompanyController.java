package com.kob.backend.controller.company.curd;

import com.kob.backend.service.company.curd.AddCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class AddCompanyController {
    @Autowired
    private AddCompanyService addCompanyService;

    @PostMapping("/company/add/")
    public Map<String, String> addCompany(@RequestParam Map<String , String> map) {
        String companyname = map.get("companyname");
        String photo = map.get("photo");
        String ownername = map.get("ownername");
        String telephone = map.get("telephone");
        String adminname = map.get("adminname");
        String note = map.get("note");
        return addCompanyService.addCompany(companyname, photo, ownername, telephone, adminname, note);
    }
}
