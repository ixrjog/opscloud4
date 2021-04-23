package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_aliyun_log")
public class OcAliyunLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 阿里云账户uid
     */
    @Column(name = "account_uid")
    private String accountUid;

    private String project;

    private String logstore;

    private String config;

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
     * 获取阿里云账户uid
     *
     * @return account_uid - 阿里云账户uid
     */
    public String getAccountUid() {
        return accountUid;
    }

    /**
     * 设置阿里云账户uid
     *
     * @param accountUid 阿里云账户uid
     */
    public void setAccountUid(String accountUid) {
        this.accountUid = accountUid;
    }

    /**
     * @return project
     */
    public String getProject() {
        return project;
    }

    /**
     * @param project
     */
    public void setProject(String project) {
        this.project = project;
    }

    /**
     * @return logstore
     */
    public String getLogstore() {
        return logstore;
    }

    /**
     * @param logstore
     */
    public void setLogstore(String logstore) {
        this.logstore = logstore;
    }

    /**
     * @return config
     */
    public String getConfig() {
        return config;
    }

    /**
     * @param config
     */
    public void setConfig(String config) {
        this.config = config;
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
}