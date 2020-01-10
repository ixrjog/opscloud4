package com.baiyi.opscloud.jumpserver.model;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private String id;

    private String password;

    private Date last_login;

    private String first_name;

    private String last_name;

    private Boolean is_active;

    private Date date_joined;
    //required
    private String username;
    //required
    private String name;
    //required
    private String email;
    //required
    private String[] groups;

    private String role;

    private String avatar;

    private String wechat;

    private String phone;

    private Boolean enable_otp;

    private String secret_key_otp;

    private String privateKey;

    private String publicKey;

    private Boolean is_first_login;

    private Date date_expired;

    private String created_by;

    private String comment;

    private String[] user_permissions;

}