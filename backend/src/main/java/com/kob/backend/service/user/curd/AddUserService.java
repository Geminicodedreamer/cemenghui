package com.kob.backend.service.user.curd;

import java.util.Map;

public interface AddUserService {
    public Map<String , String> addUser(String username , String nickname , String password , String telephone , String apartmentname , String gender , Integer status , String email , String role , String note , String companyname , String post , String photo , String createtime);
}
