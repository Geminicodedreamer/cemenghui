package com.kob.backend.service.user.curd;

import com.alibaba.fastjson.JSONObject;

public interface SearchUserService {
    public JSONObject searchuserlist(Integer page ,String companyname , String username , String telephone , Integer status , String starttime , String endtime);
}
