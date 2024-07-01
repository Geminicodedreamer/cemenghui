package com.kob.backend.service.user.curd;

import java.util.Map;

public interface ModifyUserService {
    public Map<String , String> modifyuser(Integer id , String nickname , String telephone , String apartmentname , String gender , Integer status , String email , String role , String note , String post , String photo);
}
