package com.kob.backend.service.impl.user.curd;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.curd.SearchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class SearchUserServiceImpl implements SearchUserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public JSONObject searchuserlist(Integer page , String companyname, String username, String telephone, Integer status, String starttime, String endtime) {
        IPage<User> userIPage = new Page<>(page , 10);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(companyname != null && !companyname.isEmpty())
        {
            queryWrapper.like("companyname" , companyname);
        }
        if(username != null && !username.isEmpty())
        {
            queryWrapper.like("username" , username);
        }
        if(telephone != null && !telephone.isEmpty())
        {
            queryWrapper.like("telephone" , telephone);
        }
        if(status != null)
        {
            queryWrapper.like("status" , status);
        }
        if(starttime != null && !starttime.isEmpty())
        {
            queryWrapper.ge("createtime" ,starttime);
        }
        if(endtime != null && !endtime.isEmpty())
        {
            queryWrapper.le("createtime" , endtime);
        }

        queryWrapper.orderByAsc("userId");
        List<User> userList = userMapper.selectPage(userIPage , queryWrapper).getRecords();
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
        resp.put("user_count" , userMapper.selectCount(queryWrapper));

        return resp;
    }
}
