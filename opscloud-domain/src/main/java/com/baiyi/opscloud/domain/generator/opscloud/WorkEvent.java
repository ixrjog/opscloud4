package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "work_event")
public class WorkEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 工作角色id
     */
    @Column(name = "work_role_id")
    private Integer workRoleId;

    /**
     * 工作类目id
     */
    @Column(name = "work_item_id")
    private Integer workItemId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 时间
     */
    @Column(name = "work_event_time")
    private Date workEventTime;

    @Column(name = "work_event_cnt")
    private Integer workEventCnt;

    private String comment;

    /**
     * 创建时间
     */
    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}