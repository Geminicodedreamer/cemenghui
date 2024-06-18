package com.kob.backend.pojo;

import lombok.Data;

@Data
public class News {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String summary;
    private String imagePath;
}
