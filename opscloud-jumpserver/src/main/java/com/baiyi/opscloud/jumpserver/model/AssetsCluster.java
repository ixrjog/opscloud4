package com.baiyi.opscloud.jumpserver.model;

import lombok.Data;

import java.util.Date;

@Data
public class AssetsCluster {
    private String id;

    private String name;

    private String bandwidth;

    private String contact;

    private String phone;

    private String address;

    private Date dateCreated;

    private String operator;

    private String intranet;

    private String extranet;

    private String comment;

    private String created_by;

    private String admin_user;

    private String[] assets;

}