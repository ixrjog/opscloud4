package com.baiyi.opscloud.domain.generator.opscloud;

import java.util.Date;
import javax.persistence.*;

@Table(name = "oc_profile_subscription")
public class OcProfileSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 订阅类型
     */
    @Column(name = "subscription_type")
    private String subscriptionType;

    /**
     * 名称
     */
    private String name;

    /**
     * 主机模式
     */
    @Column(name = "host_pattern")
    private String hostPattern;

    private String comment;

    /**
     * 最近执行的任务id
     */
    @Column(name = "server_task_id")
    private Integer serverTaskId;

    @Column(name = "execution_time")
    private Date executionTime;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 变量
     */
    private String vars;

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
     * 获取订阅类型
     *
     * @return subscription_type - 订阅类型
     */
    public String getSubscriptionType() {
        return subscriptionType;
    }

    /**
     * 设置订阅类型
     *
     * @param subscriptionType 订阅类型
     */
    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    /**
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取主机模式
     *
     * @return host_pattern - 主机模式
     */
    public String getHostPattern() {
        return hostPattern;
    }

    /**
     * 设置主机模式
     *
     * @param hostPattern 主机模式
     */
    public void setHostPattern(String hostPattern) {
        this.hostPattern = hostPattern;
    }

    /**
     * @return comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 获取最近执行的任务id
     *
     * @return server_task_id - 最近执行的任务id
     */
    public Integer getServerTaskId() {
        return serverTaskId;
    }

    /**
     * 设置最近执行的任务id
     *
     * @param serverTaskId 最近执行的任务id
     */
    public void setServerTaskId(Integer serverTaskId) {
        this.serverTaskId = serverTaskId;
    }

    /**
     * @return execution_time
     */
    public Date getExecutionTime() {
        return executionTime;
    }

    /**
     * @param executionTime
     */
    public void setExecutionTime(Date executionTime) {
        this.executionTime = executionTime;
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

    /**
     * 获取变量
     *
     * @return vars - 变量
     */
    public String getVars() {
        return vars;
    }

    /**
     * 设置变量
     *
     * @param vars 变量
     */
    public void setVars(String vars) {
        this.vars = vars;
    }
}