package com.kob.backend.service.impl.company.curd;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.CompanyMapper;
import com.kob.backend.pojo.Company;
import com.kob.backend.service.company.curd.ModifyCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Struct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ModifyCompanyServiceImpl implements ModifyCompanyService {
    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public Map<String, String> modifycompany(Integer id, String companyname, String photo, String ownername, String telephone, String note) {
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

        if (companyname.length() > 100) {
            map.put("error_message", "企业名称长度不能大于100");
            return map;
        }
        QueryWrapper<Company> querywrapper_id = new QueryWrapper<>();
        querywrapper_id.eq("id" , id);
        Company company = companyMapper.selectOne(querywrapper_id);
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("companyname", companyname);
        List<Company> companies = companyMapper.selectList(queryWrapper);
        if (!companies.isEmpty() && !companyname.equals(company.getCompanyname())) {
            map.put("error_message", "企业名称已存在");
            return map;
        }

        Company new_company = new Company(id , companyname , company.getPassword() , photo , ownername , telephone , company.getAdminname(), company.getSymbol() , note);
        companyMapper.updateById(new_company);

        map.put("error_message" , "success");
        return map;
    }

    @Override
    public Map<String, String> modifyAdmin(Integer companyId, String adminname) {
        Map<String, String> map = new HashMap<>();
        if (adminname == null || adminname.trim().isEmpty()) {
            map.put("error_message", "管理员名字不能为空");
            return map;
        }

        Company company = companyMapper.selectById(companyId);
        if (company == null) {
            map.put("error_message", "公司不存在");
            return map;
        }

        company.setAdminname(adminname);
        companyMapper.updateById(company);

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
