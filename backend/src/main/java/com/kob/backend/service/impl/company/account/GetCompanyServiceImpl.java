package com.kob.backend.service.impl.company.account;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.CompanyMapper;
import com.kob.backend.pojo.Company;
import com.kob.backend.service.company.account.GetCompanyListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class GetCompanyServiceImpl implements GetCompanyListService {
    @Autowired
    private CompanyMapper companyMapper;
    @Override
    public JSONObject getcompanylist(Integer page) {
        IPage<Company> companyIPage = new Page<>(page , 10);
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        List<Company> companyList = companyMapper.selectPage(companyIPage, queryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        List<JSONObject> items = new LinkedList<>();
        for (Company company: companyList) {
            JSONObject item = new JSONObject();
            item.put("companyId" , company.getId());
            item.put("companyName" , company.getCompanyname());
            item.put("photo" , company.getPhoto());
            item.put("telephone" , company.getTelephone());
            item.put("adminname" , company.getAdminname());
            item.put("symbol" , company.getSymbol());
            item.put("ownername" , company.getOwnername());
            items.add(item);
        }
        resp.put("companys", items);
        resp.put("company_count", companyMapper.selectCount(null));


        return resp;

    }
}
