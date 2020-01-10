package com.baiyi.opscloud.jumpserver.model;

import lombok.Data;

import java.util.Date;

@Data
public class SystemUser {
    private String id;

    private String name;

    private String username;

    private String password;

    private String private_key;

    private String public_key;

    private String comment;

    private Date date_created;

    private Date date_updated;

    private String created_by;

    private Integer priority;

    private String protocol;

    private Boolean auto_push;

    private String sudo;

    private String shell;

    private String[] nodes;

    private String login_mode;

}