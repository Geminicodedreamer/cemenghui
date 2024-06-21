package com.kob.backend.service.company.account;

import java.util.Map;

public interface RegisterService {
    public Map<String, String> register(String companyname, String password, String confirmedPassword , String telephone);
}
