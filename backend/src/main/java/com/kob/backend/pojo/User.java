package com.kob.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @TableId(type = IdType.AUTO)
    private Integer userid;
    private String username;
    private String nickname;
    private String password;
    private String photo;
    private String telephone;
    private String apartmentname;
    private String gender;
    private Integer status;
    private String email;
    private String role;
    private String companyname;
    private String post;
    private String note;
}
