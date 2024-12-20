package com.kob.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String companyname;
    private String password;
    private String photo;
    private String ownername;
    private String telephone;
    private String adminname;
    private Integer symbol;
    private String note;
}
