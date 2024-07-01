package com.kob.backend.service.organization.curd;

import java.util.Map;

public interface ModifyOrganizationService {
    public Map<String ,String > modifyorganization(Integer id,String uporganization,String organitionname, String charger,String telephone,String email,String status);
}
