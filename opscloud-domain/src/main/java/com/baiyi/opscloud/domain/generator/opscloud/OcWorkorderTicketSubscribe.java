package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_workorder_ticket_subscribe")
public class OcWorkorderTicketSubscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 工单id
     */
    @Column(name = "ticket_id")
    private Integer ticketId;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 订阅类型
     */
    @Column(name = "subscribe_type")
    private Integer subscribeType;

    /**
     * 有效
     */
    @Column(name = "subscribe_active")
    private Boolean subscribeActive;

    /**
     * 说明
     */
    private String comment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取工单id
     *
     * @return ticket_id - 工单id
     */
    public Integer getTicketId() {
        return ticketId;
    }

    /**
     * 设置工单id
     *
     * @param ticketId 工单id
     */
    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    /**
     * 获取用户id
     *
     * @return user_id - 用户id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户id
     *
     * @param userId 用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取用户名
     *
     * @return username - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取订阅类型
     *
     * @return subscribe_type - 订阅类型
     */
    public Integer getSubscribeType() {
        return subscribeType;
    }

    /**
     * 设置订阅类型
     *
     * @param subscribeType 订阅类型
     */
    public void setSubscribeType(Integer subscribeType) {
        this.subscribeType = subscribeType;
    }

    /**
     * 获取有效
     *
     * @return subscribe_active - 有效
     */
    public Boolean getSubscribeActive() {
        return subscribeActive;
    }

    /**
     * 设置有效
     *
     * @param subscribeActive 有效
     */
    public void setSubscribeActive(Boolean subscribeActive) {
        this.subscribeActive = subscribeActive;
    }

    /**
     * 获取说明
     *
     * @return comment - 说明
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置说明
     *
     * @param comment 说明
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}