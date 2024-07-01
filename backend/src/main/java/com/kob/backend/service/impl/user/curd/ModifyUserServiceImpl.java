package com.kob.backend.service.impl.user.curd;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.curd.ModifyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ModifyUserServiceImpl implements ModifyUserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public Map<String, String> modifyuser(Integer id, String nickname, String telephone, String apartmentname, String gender, Integer status, String email, String role, String note, String post, String photo) {
        Map<String , String> map = new HashMap<>();
        if(nickname == null){
            map.put("error_message", "用户昵称不能为空");
            return map;
        }
        if(email != null && !isEmail(email)){
            map.put("error_message" , "邮箱格式错误");
            return map;
        }

        if(telephone != null && !isMobile(telephone))
        {
            map.put("error_message" , "电话格式错误");
            return map;
        }

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userId" , id);
        User user = userMapper.selectById(id);

        User new_user = new User(id , user.getUsername() , nickname , user.getPassword() , photo , telephone , apartmentname , gender , status , email , role , user.getCompanyname() , post , note , user.getCreatetime());
        userMapper.updateById(new_user);

        map.put("error_message" , "success");
        return map;
    }


    private boolean isEmail(String email){
        Pattern rfc2822 = Pattern.compile(
                "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"
        );
        return rfc2822.matcher(email).matches();
    }

    public static Boolean isMobile(String mobile) {
        String regex = "^((13[0-9])|(14[579])|(15([0-3]|[5-9]))|(16[56])|(17[0-8])|(18[0-9])|(19[1589]))\\d{8}$";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(mobile);
        return m.matches();
    }
}
