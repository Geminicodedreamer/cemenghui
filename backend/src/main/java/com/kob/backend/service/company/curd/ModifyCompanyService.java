package com.kob.backend.service.company.curd;

import java.util.Map;

public interface ModifyCompanyService {
    public Map<String , String> modifycompany(Integer id , String companyname , String photo , String ownername , String telephone , String note);
}
