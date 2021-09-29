package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "aliyun_log_member")
public class AliyunLogMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * sls表主键
     */
    @Column(name = "aliyun_log_id")
    private Integer aliyunLogId;

    @Column(name = "server_group_id")
    private Integer serverGroupId;

    @Column(name = "server_group_name")
    private String serverGroupName;

    private String topic;

    /**
     * 环境类型
     */
    @Column(name = "env_type")
    private Integer envType;

    private String comment;

    /**
     * 最后推送时间
     */
    @Column(name = "last_push_time")
    private Date lastPushTime;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}