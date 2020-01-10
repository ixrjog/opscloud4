package com.baiyi.opscloud.jumpserver.model;

import lombok.Data;

import java.util.Date;

@Data
public class AssetsPermission {
    private String id;

    private String name;

    private Boolean is_active;

    private Date date_expired;

    private String created_by;

    private Date date_created;

    private Date date_start;

    private String comment;

    private String[] user_groups;

    private String[] users;

    private String[] nodes;

    private String[] assets;

    private String[] system_users;

}