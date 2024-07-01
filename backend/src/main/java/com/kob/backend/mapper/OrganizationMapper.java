package com.kob.backend.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.pojo.Organization;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrganizationMapper extends BaseMapper<Organization> {
}
