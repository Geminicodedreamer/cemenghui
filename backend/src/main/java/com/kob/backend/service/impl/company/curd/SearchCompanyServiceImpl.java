package com.kob.backend.service.impl.company.curd;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.CompanyMapper;
import com.kob.backend.pojo.Company;
import com.kob.backend.service.company.curd.SearchCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class SearchCompanyServiceImpl implements SearchCompanyService {
    @Autowired
    private CompanyMapper companyMapper;
    @Override
    public JSONObject searchcompany(Integer page , String symbol, String ownername, String telephone, String companyname) {
        IPage<Company> companyIPage = new Page<>(page , 10);
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        if(symbol != null && !symbol.isEmpty()){
            queryWrapper.like("symbol" , symbol);
        }
        if(ownername != null && !ownername.isEmpty())
        {
            queryWrapper.like("ownername" , ownername);
        }
        if(telephone != null && !telephone.isEmpty())
        {
            queryWrapper.like("telephone" , telephone);
        }
        if(companyname != null && !companyname.isEmpty())
        {
            queryWrapper.like("companyname" , companyname);
        }

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
            item.put("note" , company.getNote());
            items.add(item);
        }
        resp.put("companys", items);
        resp.put("company_count", companyMapper.selectCount(queryWrapper));


        return resp;
    }
}
