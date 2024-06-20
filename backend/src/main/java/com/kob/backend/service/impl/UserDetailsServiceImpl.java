package com.kob.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.CompanyMapper;
import com.kob.backend.pojo.Company;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public UserDetails loadUserByUsername(String companyname) throws UsernameNotFoundException {
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("companyname", companyname);
        Company company = companyMapper.selectOne(queryWrapper);
        if (company == null) {
            throw new RuntimeException("企业不存在");
        }

        return new UserDetailsImpl(company);
    }
}
