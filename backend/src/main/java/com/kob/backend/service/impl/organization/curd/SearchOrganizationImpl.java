package com.kob.backend.service.impl.organization.curd;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.OrganizationMapper;
import com.kob.backend.pojo.Organization;
import com.kob.backend.service.organization.curd.SearchOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class SearchOrganizationImpl implements SearchOrganizationService {
    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public JSONObject searchorganization( Integer page,String organitionname, String status) {
        IPage<Organization> organizationIPage =new Page<>(page,10);
        QueryWrapper<Organization> queryWrapper=new QueryWrapper<>();

        if (organitionname !=null && !organitionname.isEmpty()){
            queryWrapper.like("organizationname",organitionname);
        }

        if(status !=null && !status.isEmpty()){
            queryWrapper.like("status",status);
        }

        queryWrapper.orderByAsc("id");
        List<Organization> organizationList =organizationMapper.selectPage(organizationIPage,queryWrapper).getRecords();
        JSONObject resp =new JSONObject();
        List<JSONObject> items=new LinkedList<>();
        for(Organization organization:organizationList){
            JSONObject item=new JSONObject();
            item.put("id",organization.getId());
            item.put("uporganization",organization.getUporganization());
            item.put("organizationname",organization.getOrganizationname());
            item.put("charger",organization.getCharger());
            item.put("telephone",organization.getTelephone());
            item.put("email",organization.getEmail());
            item.put("status",organization.getStatus());
            item.put("creattime",organization.getCreattime());
            items.add(item);
        }
        resp.put("organizations",items);
        resp.put("organization_count",organizationMapper.selectCount(queryWrapper));

        return  resp;

    }
}
