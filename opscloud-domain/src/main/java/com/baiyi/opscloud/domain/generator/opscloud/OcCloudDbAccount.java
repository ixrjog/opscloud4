package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_cloud_db_account")
public class OcCloudDbAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 云数据库id
     */
    @Column(name = "cloud_db_id")
    private Integer cloudDbId;

    /**
     * 云数据库实例id
     */
    @Column(name = "db_instance_id")
    private String dbInstanceId;

    /**
     * 账户名称
     */
    @Column(name = "account_name")
    private String accountName;

    /**
     * 密码
     */
    @Column(name = "account_password")
    private String accountPassword;

    /**
     * 账号权限
     */
    @Column(name = "account_privilege")
    private String accountPrivilege;

    /**
     * 账号类型，取值：\n\nNormal：普通账号\nSuper：高权限账号\nSysadmin：具备超级权限（SA）的账号（仅SQL Server支持）\n默认值：Normal。
     */
    @Column(name = "account_type")
    private String accountType;

    /**
     * 允许工作流申请
     */
    private Integer workflow;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 备注
     */
    private String comment;

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
     * 获取云数据库id
     *
     * @return cloud_db_id - 云数据库id
     */
    public Integer getCloudDbId() {
        return cloudDbId;
    }

    /**
     * 设置云数据库id
     *
     * @param cloudDbId 云数据库id
     */
    public void setCloudDbId(Integer cloudDbId) {
        this.cloudDbId = cloudDbId;
    }

    /**
     * 获取云数据库实例id
     *
     * @return db_instance_id - 云数据库实例id
     */
    public String getDbInstanceId() {
        return dbInstanceId;
    }

    /**
     * 设置云数据库实例id
     *
     * @param dbInstanceId 云数据库实例id
     */
    public void setDbInstanceId(String dbInstanceId) {
        this.dbInstanceId = dbInstanceId;
    }

    /**
     * 获取账户名称
     *
     * @return account_name - 账户名称
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * 设置账户名称
     *
     * @param accountName 账户名称
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * 获取密码
     *
     * @return account_password - 密码
     */
    public String getAccountPassword() {
        return accountPassword;
    }

    /**
     * 设置密码
     *
     * @param accountPassword 密码
     */
    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    /**
     * 获取账号权限
     *
     * @return account_privilege - 账号权限
     */
    public String getAccountPrivilege() {
        return accountPrivilege;
    }

    /**
     * 设置账号权限
     *
     * @param accountPrivilege 账号权限
     */
    public void setAccountPrivilege(String accountPrivilege) {
        this.accountPrivilege = accountPrivilege;
    }

    /**
     * 获取账号类型，取值：\n\nNormal：普通账号\nSuper：高权限账号\nSysadmin：具备超级权限（SA）的账号（仅SQL Server支持）\n默认值：Normal。
     *
     * @return account_type - 账号类型，取值：\n\nNormal：普通账号\nSuper：高权限账号\nSysadmin：具备超级权限（SA）的账号（仅SQL Server支持）\n默认值：Normal。
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * 设置账号类型，取值：\n\nNormal：普通账号\nSuper：高权限账号\nSysadmin：具备超级权限（SA）的账号（仅SQL Server支持）\n默认值：Normal。
     *
     * @param accountType 账号类型，取值：\n\nNormal：普通账号\nSuper：高权限账号\nSysadmin：具备超级权限（SA）的账号（仅SQL Server支持）\n默认值：Normal。
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    /**
     * 获取允许工作流申请
     *
     * @return workflow - 允许工作流申请
     */
    public Integer getWorkflow() {
        return workflow;
    }

    /**
     * 设置允许工作流申请
     *
     * @param workflow 允许工作流申请
     */
    public void setWorkflow(Integer workflow) {
        this.workflow = workflow;
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
     * 获取备注
     *
     * @return comment - 备注
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置备注
     *
     * @param comment 备注
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}