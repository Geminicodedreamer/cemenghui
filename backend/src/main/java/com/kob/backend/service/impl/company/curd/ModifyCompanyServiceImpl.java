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

        QueryWrapper<Company> querywrapper_id = new QueryWrapper<>();
        querywrapper_id.eq("id" , id);
        Company company = companyMapper.selectOne(queryWrapper);
        Company new_company = new Company(id , companyname , company.getPassword() , photo , ownername , telephone , company.getAdminname(), company.getSymbol() , note);
        companyMapper.updateById(new_company);

        map.put("error_message" , "success");
        return map;
    }
}
