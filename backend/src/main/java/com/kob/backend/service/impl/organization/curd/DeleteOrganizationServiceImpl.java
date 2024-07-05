package com.kob.backend.service.impl.organization.curd;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.CompanyMapper;
import com.kob.backend.mapper.OrganizationMapper;
import com.kob.backend.pojo.Company;
import com.kob.backend.pojo.Organization;
import com.kob.backend.service.organization.curd.DeleteOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteOrganizationServiceImpl implements DeleteOrganizationService {
    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public JSONObject deleteorganition(Integer id) {
        QueryWrapper<Organization> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("id",id);

        Organization organization = organizationMapper.selectOne(queryWrapper);
        QueryWrapper<Company> companyQueryWrapper = new QueryWrapper<>();
        companyQueryWrapper.eq("companyname" , organization.getOrganizationname());
        companyMapper.delete(companyQueryWrapper);

        organizationMapper.delete(queryWrapper);
        JSONObject resp =new JSONObject();
        resp.put("error_message","success");
        return resp;
    }
}
