package com.kob.backend.service.organization.curd;


import java.util.Map;

public interface AddOrganizationService {

    public Map<String ,String > addOrganization(String uporganition,String organizationname, Integer id,String charger,String telephone,String email,String status,String creattime);
}
