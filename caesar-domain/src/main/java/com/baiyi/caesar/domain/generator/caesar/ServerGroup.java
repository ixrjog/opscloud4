package com.baiyi.caesar.domain.generator.caesar;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "server_group")
public class ServerGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 服务器组名称
     */
    private String name;

    /**
     * 服务器组类型
     */
    @Column(name = "server_group_type_id")
    private Integer serverGroupTypeId;

    /**
     * 允许工单申请
     */
    @Column(name = "allow_workorder")
    private Boolean allowWorkorder;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    private String comment;
}