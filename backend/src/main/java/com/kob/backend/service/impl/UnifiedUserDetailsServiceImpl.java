package com.kob.backend.service.impl;

import com.kob.backend.mapper.CompanyMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Company;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.CompanyDetailsImpl;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

@Service
public class UnifiedUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<Company> companyQuery = new QueryWrapper<>();
        companyQuery.eq("companyname", username);
        Company company = companyMapper.selectOne(companyQuery);
        if (company != null) {
            return new CompanyDetailsImpl(company);
        }

        QueryWrapper<User> userQuery = new QueryWrapper<>();
        userQuery.eq("username", username);
        User user = userMapper.selectOne(userQuery);
        if (user != null) {
            return new UserDetailsImpl(user);
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
