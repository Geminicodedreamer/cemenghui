package com.kob.backend.service.company.account;

import java.util.Map;

public interface LoginService {
    public Map<String, String> getToken(String companyname, String password);
}
