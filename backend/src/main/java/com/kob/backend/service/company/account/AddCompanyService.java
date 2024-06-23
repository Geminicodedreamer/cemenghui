package com.kob.backend.service.company.account;

import java.util.Map;

public interface AddCompanyService {
    public Map<String, String> addCompany(String companyname, String photo , String ownername,  String telephone , String adminname , String note);
}
