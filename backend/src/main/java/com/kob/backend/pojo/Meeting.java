package com.kob.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meeting {
    @TableId(type = IdType.AUTO)
    private Integer meetingid;
    private String meetingname;
    private String creator;
    private String content;
    private String starttime;
    private String endtime;
    private String photo;
    private String companyname;
}
