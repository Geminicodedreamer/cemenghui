package com.kob.backend.service.user.account;

import java.util.Map;

public interface ChangePasswordService {
    public Map<String , String> changepassword(Integer id , String password , String newpassword , String confirmnewpassword);
}
