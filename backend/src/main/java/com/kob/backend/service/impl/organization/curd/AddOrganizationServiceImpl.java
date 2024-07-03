package com.kob.backend.service.impl.organization.curd;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.OrganizationMapper;
import com.kob.backend.pojo.Organization;
import com.kob.backend.service.organization.curd.AddOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AddOrganizationServiceImpl implements AddOrganizationService {
    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public Map<String, String> addOrganization(String uporganization, String organizationname, Integer id, String charger, String telephone, String email, String status,String creattime) {
      Map<String ,String > map =new HashMap<>();

      if (uporganization ==null){
          map.put("error_message","上级部门不能为空");
          return map;
      }

      if(organizationname ==null){
          map.put("error_message","部门名称不能为空");
          return map;
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

        QueryWrapper<Organization> queryWrapper1 =new QueryWrapper<>();
        queryWrapper1.eq("organizationname",uporganization);
        if(organizationMapper.selectList(queryWrapper1).isEmpty())
        {
            map.put("error_message","父公司/部门不存在");
            return map;
        }

        QueryWrapper<Organization> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("organizationname",organizationname);
        List<Organization> organizations =organizationMapper.selectList(queryWrapper);
        if(!organizations.isEmpty()){
            map.put("error_message","部门名称已存在");
            return map;
        }


        creattime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Organization organization =new Organization(null,uporganization,organizationname,charger,telephone,email,status,creattime);
        organizationMapper.insert(organization);
        map.put("error_message","success");

        return map;
    }
}
