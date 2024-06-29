package com.kob.backend.service.impl.user.curd;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.curd.AddUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AddUserServiceImpl implements AddUserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> addUser(String username, String photo, String password) {
        Map<String , String> map = new HashMap<>();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username" , username);
        List<User> users = userMapper.selectList(queryWrapper);
        if(!users.isEmpty())
        {
            map.put("error_message" , "用户名已存在");
            return map;
        }
        String passwordencoding = passwordEncoder.encode(password);
        User user = new User(null , username , null , passwordencoding , photo , null , null , null , null , null , null , null , null , null);
        userMapper.insert(user);

        map.put("error_message", "success");
        return map;
    }
}
