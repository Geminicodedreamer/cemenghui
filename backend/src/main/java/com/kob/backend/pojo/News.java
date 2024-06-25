package com.kob.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {
    @TableId(type= IdType.AUTO)
    private Integer newsid;
    // 新闻标题
    private String title;
    // 新闻简介
    private String summary;
    // 新闻图片路径
    private String imagePath;
    // 新闻内容
    private String content;
    // 作者
    private String author;
    // 所属租户
    private String tenant;
}
