package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_profile_subscription")
public class OcProfileSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 订阅类型
     */
    @Column(name = "subscription_type")
    private String subscriptionType;

    @Column(name = "server_group_id")
    private Integer serverGroupId;

    /**
     * 主机模式
     */
    @Column(name = "host_pattern")
    private String hostPattern;

    /**
     * playbook脚本id
     */
    @Column(name = "script_id")
    private Integer scriptId;

    /**
     * 最近执行的任务id
     */
    @Column(name = "server_task_id", insertable = false, updatable = false)
    private Integer serverTaskId;

    @Column(name = "execution_time", insertable = false, updatable = false)
    private Date executionTime;

    private String comment;

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
     * @return server_group_id
     */
    public Integer getServerGroupId() {
        return serverGroupId;
    }

    /**
     * @param serverGroupId
     */
    public void setServerGroupId(Integer serverGroupId) {
        this.serverGroupId = serverGroupId;
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
     * 获取playbook脚本id
     *
     * @return script_id - playbook脚本id
     */
    public Integer getScriptId() {
        return scriptId;
    }

    /**
     * 设置playbook脚本id
     *
     * @param scriptId playbook脚本id
     */
    public void setScriptId(Integer scriptId) {
        this.scriptId = scriptId;
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