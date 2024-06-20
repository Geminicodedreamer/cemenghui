package com.kob.backend.service.impl.company.account;

import com.kob.backend.pojo.Company;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.InfoService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InfoServiceImpl implements InfoService {
    @Override
    public Map<String, String> getinfo() {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        Company company = loginUser.getCompany();

        Map<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("id", company.getId().toString());
        map.put("companyname", company.getCompanyname());
        map.put("photo", company.getPhoto());
        return map;
    }
}
