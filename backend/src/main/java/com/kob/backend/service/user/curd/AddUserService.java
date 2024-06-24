package com.kob.backend.service.user.curd;

import java.util.Map;

public interface AddUserService {
    public Map<String , String> addUser(String username , String photo , String password);
}
