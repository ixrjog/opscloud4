package com.baiyi.opscloud.jumpserver.model;

import lombok.Data;

import java.util.Date;

@Data
public class Usergroup {
    private String id;

    private Boolean is_discard;

    private Date discard_time;

    private String name;

    private Date date_created;

    private String created_by;

    private String comment;

}