package com.kob.backend.service.company.curd;

import com.alibaba.fastjson.JSONObject;

public interface SearchCompanyService {
    public JSONObject searchcompany(Integer page , String symbol , String ownername , String telephone , String companyname);
}
