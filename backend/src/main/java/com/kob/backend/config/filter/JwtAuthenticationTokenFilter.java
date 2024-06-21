package com.kob.backend.config.filter;

import com.kob.backend.mapper.CompanyMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Company;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.CompanyDetailsImpl;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token) || !token.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        token = token.substring(7);

        String userid;
        String userType;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
            userType = claims.get("userType", String.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.err.println(userType);

        UserDetails loginUser;
        UsernamePasswordAuthenticationToken authenticationToken;
        if ("Company".equals(userType)) {
            Company company = companyMapper.selectById(Integer.parseInt(userid));
            if (company == null) {
                throw new RuntimeException("未登录");
            }
            loginUser = new CompanyDetailsImpl(company);
            authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        } else {
            User user = userMapper.selectById(Integer.parseInt(userid));
            if (user == null) {
                throw new RuntimeException("未登录");
            }
            loginUser = new UserDetailsImpl(user);
            authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        }
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
