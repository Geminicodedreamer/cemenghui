package com.kob.backend.service.impl.company.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.CompanyMapper;
import com.kob.backend.pojo.Company;
import com.kob.backend.service.company.account.ChangepasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChangepasswordServiceImpl implements ChangepasswordService {
    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public Map<String, String> changepassword(Integer id, String password, String newpassword , String confirmnewpassword) {
        Map<String , String> map = new HashMap<>();

        if(newpassword.equals(confirmnewpassword)){
            map.put("error_message" , "两次密码输入不一致");
            return map;
        }

        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id" , id);
        Company company = companyMapper.selectOne(queryWrapper);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if(!encoder.matches(password , company.getPassword()))
        {
            map.put("error_message" , "原密码错误");
            return map;
        }

        if(encoder.matches(newpassword , company.getPassword()))
        {
            map.put("error_message" , "新密码不能与原密码一致");
            return map;
        }

        company.setPassword(passwordEncoder.encode(newpassword));
        companyMapper.updateById(company);

        map.put("error_message" , "success");
        return null;
    }
}
