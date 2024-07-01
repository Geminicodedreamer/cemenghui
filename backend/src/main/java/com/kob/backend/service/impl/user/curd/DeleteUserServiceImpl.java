package com.kob.backend.service.impl.user.curd;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.curd.DeleteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserServiceImpl implements DeleteUserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public JSONObject deleteuser(Integer id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId" , id);
        userMapper.delete(queryWrapper);
        JSONObject resp = new JSONObject();
        resp.put("error_message" , "success");
        return resp;
    }
}
