package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Company;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.ChangePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChangePasswordServiceImpl implements ChangePasswordService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public Map<String, String> changepassword(Integer id, String password, String newpassword , String confirmnewpassword) {
        Map<String , String> map = new HashMap<>();

        if(!newpassword.equals(confirmnewpassword)){
            map.put("error_message" , "两次密码输入不一致");
            return map;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId" , id);
        User user = userMapper.selectOne(queryWrapper);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if(!encoder.matches(password , user.getPassword()))
        {
            map.put("error_message" , "原密码错误");
            return map;
        }

        if(encoder.matches(newpassword , user.getPassword()))
        {
            map.put("error_message" , "新密码不能与原密码一致");
            return map;
        }

        user.setPassword(passwordEncoder.encode(newpassword));
        userMapper.updateById(user);

        map.put("error_message" , "success");
        return map;
    }
}
