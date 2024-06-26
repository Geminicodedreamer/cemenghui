package com.kob.backend.service.impl.company.curd;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.CompanyMapper;
import com.kob.backend.pojo.Company;
import com.kob.backend.service.company.curd.AddCompanyService;
import com.kob.backend.utils.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AddCompanyServiceImpl implements AddCompanyService {
    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String , String> addCompany(String companyname, String photo, String ownername, String telephone, String adminname, String note) {
        Map<String , String> map = new HashMap<>();
        if (companyname == null) {
            map.put("error_message", "企业名称不能为空");
            return map;
        }

        if (ownername == null) {
            map.put("error_message", "联系人不能为空");
            return map;
        }

        if (telephone == null) {
            map.put("error_message", "电话不能为空");
            return map;
        }

        if (!isMobile(telephone)) {
            map.put("error_message", "电话格式错误");
            return map;
        }

        if (adminname == null) {
            map.put("error_message", "管理员不能为空");
            return map;
        }

        if (companyname.length() > 100) {
            map.put("error_message", "企业名称长度不能大于100");
            return map;
        }

        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("companyname", companyname);
        List<Company> companies = companyMapper.selectList(queryWrapper);
        if (!companies.isEmpty()) {
            map.put("error_message", "企业名称已存在");
            return map;
        }

        Random random = new Random();
        Integer symbol = random.nextInt(900000000) + 100000000;
        String encodedPassword = passwordEncoder.encode(symbol.toString());

        Company company = new Company(null, companyname, encodedPassword, photo, ownername, telephone, adminname, symbol, note);
        companyMapper.insert(company);

        map.put("error_message", "success");
        return map;
    }

    public static Boolean isMobile(String mobile) {
        String regex = "^((13[0-9])|(14[579])|(15([0-3]|[5-9]))|(16[56])|(17[0-8])|(18[0-9])|(19[1589]))\\d{8}$";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(mobile);
        return m.matches();
    }
}
