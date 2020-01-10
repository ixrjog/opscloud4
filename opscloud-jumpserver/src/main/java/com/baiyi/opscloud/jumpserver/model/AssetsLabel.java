package com.baiyi.opscloud.jumpserver.model;

import lombok.Data;

import java.util.Date;

@Data
public class AssetsLabel {
    private String id;

    private String name;

    private String value;

    private String category;

    private Boolean is_active;

    private Date date_created;

    private String comment;

}