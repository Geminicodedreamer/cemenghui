package com.kob.backend.pojo;

import lombok.Data;

@Data
public class Conference {
    private Long id;
    private String title;
    private String description;
    private String date;
    private String location;
    private String organizer;
}
