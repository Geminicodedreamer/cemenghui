package com.kob.backend.service.organization.curd;

import com.alibaba.fastjson.JSONObject;

public interface SearchOrganizationService {
    public JSONObject searchorganization(Integer page,String organitionname,String status);
}
