package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "aliyun_log")
public class AliyunLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 数据源实例id
     */
    @Column(name = "datasource_instance_id")
    private Integer datasourceInstanceId;

    private String project;

    private String logstore;

    private String config;

    private String comment;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}