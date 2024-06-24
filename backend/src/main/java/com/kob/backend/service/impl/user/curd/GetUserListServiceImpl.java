package com.kob.backend.service.impl.user.curd;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.curd.GetUserListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class GetUserListServiceImpl implements GetUserListService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getuserlist(Integer page) {
        IPage<User> userIPage = new Page<>(page , 10);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("userId");
        List<User> userList = userMapper.selectPage(userIPage , queryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        List<JSONObject> items = new LinkedList<>();
        for(User user : userList)
        {
            JSONObject item = new JSONObject();
            item.put("userId", user.getUserid());
            item.put("username" , user.getUsername());
            items.add(item);
        }
        resp.put("users" , items);
        resp.put("user_count" , userMapper.selectCount(null));

        return resp;
    }
}
