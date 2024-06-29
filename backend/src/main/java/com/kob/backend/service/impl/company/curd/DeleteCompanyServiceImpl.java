package com.kob.backend.service.impl.company.curd;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.CompanyMapper;
import com.kob.backend.pojo.Company;
import com.kob.backend.service.company.curd.DeleteCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteCompanyServiceImpl implements DeleteCompanyService {
    @Autowired
    private CompanyMapper companyMapper;
    @Override
    public JSONObject deletecompany(Integer id) {
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id" , id);
        companyMapper.delete(queryWrapper);
        JSONObject resp = new JSONObject();
        resp.put("error_message" , "success");
        return resp;
    }
}
