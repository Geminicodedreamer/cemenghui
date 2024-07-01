package com.kob.backend.service.impl.organization.curd;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.OrganizationMapper;
import com.kob.backend.pojo.Organization;
import com.kob.backend.service.organization.curd.ModifyOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ModifyOrganizationServiceImpl implements ModifyOrganizationService {
    @Autowired
    private OrganizationMapper organizationMapper;


    @Override
    public Map<String, String> modifyorganization(Integer id,String uporganization, String organizationname, String charger, String telephone, String email, String status) {
        Map<String ,String> map =new HashMap<>();
        if (organizationname ==null){
            map.put("error_message","部门名称不能为空");
        }

        if(charger ==null){
            map.put("error_message","负责人不能为空");
            return map;
        }

        if (telephone==null){
            map.put("error_message","联系电话不能为空");
        }

        if(email ==null){
            map.put("error_message","邮箱不能为空");
        }

        if (organizationname.length() >100){
            map.put("error_message","部门长度不能大于100");
            return map;
        }

        QueryWrapper<Organization> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("organizationname",organizationname);
        QueryWrapper<Organization> queryWrapper_id=new QueryWrapper<>();
        queryWrapper_id.eq("id",id);
        Organization organization=organizationMapper.selectOne(queryWrapper_id);
        List<Organization> organizations =organizationMapper.selectList(queryWrapper);
        if(!organizations.isEmpty() && !organization.getOrganizationname().equals(organizationname)){
            map.put("error_message","部门名称已存在");
            return map;
        }



        Organization new_organization =new Organization(id,uporganization,organizationname,charger,telephone,email,status,null);
        organizationMapper.updateById(new_organization);

        map.put("error_message","success");
        return map;
    }
}
