package com.sdg.cmdb.service.jms.model;

import lombok.Data;

import java.util.Date;

@Data
public class AdminUser {
    private String id;
    private String name;
    private String username;
    private String password;
    private Date date_created;
    private Date date_updated;
    private String created_by;
    private String private_key;
    private String public_key;
    private Boolean become;
    private String become_method;
    private String become_user;
    private String _become_pass;
    private String comment;
    private String[] clusters;
}