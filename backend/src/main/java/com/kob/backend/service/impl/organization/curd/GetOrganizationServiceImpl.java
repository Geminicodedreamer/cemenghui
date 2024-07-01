package com.kob.backend.service.impl.organization.curd;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.OrganizationMapper;
import com.kob.backend.pojo.Organization;
import com.kob.backend.service.organization.curd.GetOrganizationListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class GetOrganizationServiceImpl implements GetOrganizationListService {
    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public JSONObject getorganizationlist() {
        QueryWrapper<Organization> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        List<Organization> organizationList = organizationMapper.selectList(queryWrapper);
        JSONObject resp = new JSONObject();
        List<JSONObject> items = new LinkedList<>();

        Map<Integer, JSONObject> orgMap = new HashMap<>();
        Map<String, Integer> nameMap = new HashMap<>();
        for (Organization organization : organizationList) {
            JSONObject item = new JSONObject();
            item.put("id", organization.getId());
            item.put("uporganization", organization.getUporganization());
            item.put("organizationname", organization.getOrganizationname());
            item.put("charger", organization.getCharger());
            item.put("telephone", organization.getTelephone());
            item.put("email", organization.getEmail());
            item.put("status", organization.getStatus());
            item.put("creattime", organization.getCreattime());
            item.put("children", new LinkedList<>());
            orgMap.put(organization.getId(), item);
            nameMap.put(organization.getOrganizationname(),organization.getId());
        }

        List<JSONObject> rootOrganizations = new LinkedList<>();
        for (Organization organization : organizationList) {
            JSONObject item = orgMap.get(organization.getId());
            if (organization.getUporganization() != null) {
                JSONObject parent = orgMap.get(nameMap.get(organization.getUporganization()));
                if (parent != null) {
                    ((List<JSONObject>) parent.get("children")).add(item);
                }
            } else {
                rootOrganizations.add(item);
            }
        }

        resp.put("organizations", rootOrganizations);
        return resp;
    }
}

