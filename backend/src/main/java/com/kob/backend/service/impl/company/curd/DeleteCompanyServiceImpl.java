package com.kob.backend.service.impl.company.curd;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.CompanyMapper;
import com.kob.backend.mapper.OrganizationMapper;
import com.kob.backend.pojo.Company;
import com.kob.backend.pojo.Organization;
import com.kob.backend.service.company.curd.DeleteCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteCompanyServiceImpl implements DeleteCompanyService {
    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public JSONObject deletecompany(Integer id) {
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id" , id);

        Company company = companyMapper.selectOne(queryWrapper);
        QueryWrapper<Organization> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("organizationname" , company.getCompanyname());
        organizationMapper.delete(queryWrapper1);

        companyMapper.delete(queryWrapper);
        JSONObject resp = new JSONObject();
        resp.put("error_message" , "success");
        return resp;
    }
}
