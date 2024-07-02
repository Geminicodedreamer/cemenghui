package com.kob.backend.service.impl.user.curd;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.CompanyMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Company;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.curd.AddUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AddUserServiceImpl implements AddUserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> addUser(String username, String nickname, String password, String telephone, String apartmentname, String gender, Integer status, String email, String role, String note, String companyname, String post, String photo, String createtime) {
        Map<String , String> map = new HashMap<>();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username" , username);
        List<User> users = userMapper.selectList(queryWrapper);
        if(!users.isEmpty())
        {
            map.put("error_message" , "用户名已存在");
            return map;
        }

        if(username == null)
        {
            map.put("error_message" , "用户名不能为空");
            return map;
        }

        if(password == null)
        {
            map.put("error_message" , "密码不能为空");
            return map;
        }

        if(nickname == null)
        {
            map.put("error_message" , "昵称不能为空");
            return map;
        }

        QueryWrapper<Company> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("companyname" , companyname);
        Company company = companyMapper.selectOne(queryWrapper1);
        if(company == null)
        {
            map.put("error_message" , "该企业不存在");
            return map;
        }

        if(email != null && !email.isEmpty() && !isEmail(email)){
            map.put("error_message" , "邮箱格式错误");
            return map;
        }

        if(telephone != null && !telephone.isEmpty() && !isMobile(telephone))
        {
            map.put("error_message" , "电话格式错误");
            return map;
        }

        String passwordencoding = passwordEncoder.encode(password);
        User user = new User(null , username , nickname , passwordencoding , photo , telephone , apartmentname , gender , status , email , role , companyname , post , note , createtime);
        userMapper.insert(user);

        map.put("error_message", "success");
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
