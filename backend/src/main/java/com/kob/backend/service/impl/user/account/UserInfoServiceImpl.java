package com.kob.backend.service.impl.user.account;

import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.UserInfoService;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Override
    public Map<String, String> getinfo() {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();

        Map<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("id", user.getUserid().toString());
        map.put("username", user.getNickname());
        map.put("photo", user.getPhoto());
        map.put("role" , user.getRole());
        map.put("userrealname" , user.getUsername());
        map.put("telephone" , user.getTelephone());
        map.put("apartmentname" , user.getApartmentname());
        map.put("gender" , user.getGender());
        map.put("email" , user.getEmail());
        map.put("note" , user.getNote());
        map.put("companyname" , user.getCompanyname());
        map.put("post" , user.getPost());
        map.put("createtime" , user.getCreatetime());
        return map;
    }
}
