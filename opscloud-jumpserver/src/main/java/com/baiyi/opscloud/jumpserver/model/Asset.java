package com.baiyi.opscloud.jumpserver.model;

import lombok.Data;

import java.util.Date;

@Data
public class Asset {
    private String id;
    private String ip;
    private String hostname;
    private String cluster_name;
    private Integer port;
    private Boolean is_active;
    private Boolean is_connective;
    private String type;
    private String env;
    private String status;
    private String public_ip;
    private String remoteCardIp;
    private String cabinetNo;
    private Integer cabinetPos;
    private String number;
    private String vendor;
    private String model;
    private String sn;
    private String cpu_model;
    private Integer cpu_count;
    private Integer cpu_cores;
    private String memory;
    private String disk_total;
    private String disk_info;
    private String platform;
    private String os;
    private String os_version;
    private String os_arch;
    private String hostname_raw;
    private String created_by;
    private Date date_created;
    private String admin_user;
    private String cluster;
    private String comment;
    private String hardware_info;
    private String[] nodes;
    private String[] labels;
    private String protocol;
}