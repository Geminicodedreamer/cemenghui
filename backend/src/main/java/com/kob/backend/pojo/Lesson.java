package com.kob.backend.pojo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String lessonname;
    private String lessonintro;
    private String lessonauthor;
    private String photo;
    private String video;
    private String companyname;
}
