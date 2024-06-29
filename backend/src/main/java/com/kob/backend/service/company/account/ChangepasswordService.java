package com.kob.backend.service.company.account;

import java.util.Map;

public interface ChangepasswordService {
    public Map<String , String> changepassword(Integer id , String password , String newpassword , String confirmnewpassword);
}
