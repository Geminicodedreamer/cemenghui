package com.kob.backend.service.impl.company.account;

import com.kob.backend.pojo.Company;
import com.kob.backend.service.impl.utils.CompanyDetailsImpl;
import com.kob.backend.service.company.account.LoginService;
import com.kob.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class LoginServiceImpl implements LoginService {

    private static final Logger logger = Logger.getLogger(LoginServiceImpl.class.getName());

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> getToken(String companyname, String password) {
        logger.info("LoginServiceImpl getToken called with companyname: " + companyname);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(companyname, password);

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);  // 登录失败，会自动处理
        CompanyDetailsImpl loginUser = (CompanyDetailsImpl) authenticate.getPrincipal();
        Company company = loginUser.getCompany();
        String jwt = JwtUtil.createJWT(company.getId().toString(), "Company");

        Map<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("token", jwt);

        return map;
    }
}
