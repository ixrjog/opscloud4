package com.baiyi.opscloud.jumpserver.model;

import lombok.Data;

import java.util.Date;

@Data
public class AssetsGroup {
    private String id;

    private String name;

    private String created_by;

    private Date date_created;

    private String comment;

    private String[] assets;

}