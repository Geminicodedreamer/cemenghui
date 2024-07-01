package com.kob.backend.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Organization {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String uporganization;
    private String organizationname;
    private String charger;
    private String telephone;
    private String email;
    private String status;
    private String creattime;
}
