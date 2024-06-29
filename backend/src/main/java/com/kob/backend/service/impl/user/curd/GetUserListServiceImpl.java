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
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("userId");
        List<User> userList = userMapper.selectList(queryWrapper);
        JSONObject resp = new JSONObject();
        List<JSONObject> items = new LinkedList<>();
        for(User user : userList)
        {
            JSONObject item = new JSONObject();
            item.put("userId", user.getUserid());
            item.put("username" , user.getUsername());
            item.put("nickname" , user.getNickname());
            item.put("telephone" , user.getTelephone());
            item.put("apartmentname" , user.getApartmentname());
            item.put("gender" , user.getGender());
            item.put("status" , user.getStatus());
            item.put("email" , user.getEmail());
            item.put("role" , user.getRole());
            item.put("note" , user.getNote());
            item.put("companyname" , user.getCompanyname());
            item.put("post" , user.getPost());
            item.put("photo" , user.getPhoto());
            item.put("createtime" , user.getCreatetime());
            items.add(item);
        }
        resp.put("users" , items);

        return resp;
    }
}
