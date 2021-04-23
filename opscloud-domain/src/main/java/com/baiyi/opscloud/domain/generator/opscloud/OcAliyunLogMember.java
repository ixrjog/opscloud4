package com.baiyi.opscloud.domain.generator.opscloud;

import java.util.Date;
import javax.persistence.*;

@Table(name = "oc_aliyun_log_member")
public class OcAliyunLogMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 阿里云账户uid
     */
    @Column(name = "log_id")
    private Integer logId;

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

    @Column(name = "last_push_time")
    private Date lastPushTime;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
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
     * 获取阿里云账户uid
     *
     * @return log_id - 阿里云账户uid
     */
    public Integer getLogId() {
        return logId;
    }

    /**
     * 设置阿里云账户uid
     *
     * @param logId 阿里云账户uid
     */
    public void setLogId(Integer logId) {
        this.logId = logId;
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
     * @return server_group_name
     */
    public String getServerGroupName() {
        return serverGroupName;
    }

    /**
     * @param serverGroupName
     */
    public void setServerGroupName(String serverGroupName) {
        this.serverGroupName = serverGroupName;
    }

    /**
     * @return topic
     */
    public String getTopic() {
        return topic;
    }

    /**
     * @param topic
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * 获取环境类型
     *
     * @return env_type - 环境类型
     */
    public Integer getEnvType() {
        return envType;
    }

    /**
     * 设置环境类型
     *
     * @param envType 环境类型
     */
    public void setEnvType(Integer envType) {
        this.envType = envType;
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
     * @return last_push_time
     */
    public Date getLastPushTime() {
        return lastPushTime;
    }

    /**
     * @param lastPushTime
     */
    public void setLastPushTime(Date lastPushTime) {
        this.lastPushTime = lastPushTime;
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