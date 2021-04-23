package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_dingtalk_dept")
public class OcDingtalkDept {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 钉钉账号id
     */
    @Column(name = "dingtalk_uid")
    private String dingtalkUid;

    /**
     * 部门ID
     */
    @Column(name = "dept_id")
    private Long deptId;

    /**
     * 部门名称
     */
    @Column(name = "dept_name")
    private String deptName;

    /**
     * 上级部门ID
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 是否同步创建一个关联此部门的企业群
     */
    @Column(name = "create_dept_group")
    private Boolean createDeptGroup;

    /**
     * 部门群已经创建后，有新人加入部门是否会自动加入该群
     */
    @Column(name = "auto_add_user")
    private Boolean autoAddUser;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 扩展字段
     */
    private String ext;

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
     * 获取钉钉账号id
     *
     * @return dingtalk_uid - 钉钉账号id
     */
    public String getDingtalkUid() {
        return dingtalkUid;
    }

    /**
     * 设置钉钉账号id
     *
     * @param dingtalkUid 钉钉账号id
     */
    public void setDingtalkUid(String dingtalkUid) {
        this.dingtalkUid = dingtalkUid;
    }

    /**
     * 获取部门ID
     *
     * @return dept_id - 部门ID
     */
    public Long getDeptId() {
        return deptId;
    }

    /**
     * 设置部门ID
     *
     * @param deptId 部门ID
     */
    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    /**
     * 获取部门名称
     *
     * @return dept_name - 部门名称
     */
    public String getDeptName() {
        return deptName;
    }

    /**
     * 设置部门名称
     *
     * @param deptName 部门名称
     */
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    /**
     * 获取上级部门ID
     *
     * @return parent_id - 上级部门ID
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置上级部门ID
     *
     * @param parentId 上级部门ID
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取是否同步创建一个关联此部门的企业群
     *
     * @return create_dept_group - 是否同步创建一个关联此部门的企业群
     */
    public Boolean getCreateDeptGroup() {
        return createDeptGroup;
    }

    /**
     * 设置是否同步创建一个关联此部门的企业群
     *
     * @param createDeptGroup 是否同步创建一个关联此部门的企业群
     */
    public void setCreateDeptGroup(Boolean createDeptGroup) {
        this.createDeptGroup = createDeptGroup;
    }

    /**
     * 获取部门群已经创建后，有新人加入部门是否会自动加入该群
     *
     * @return auto_add_user - 部门群已经创建后，有新人加入部门是否会自动加入该群
     */
    public Boolean getAutoAddUser() {
        return autoAddUser;
    }

    /**
     * 设置部门群已经创建后，有新人加入部门是否会自动加入该群
     *
     * @param autoAddUser 部门群已经创建后，有新人加入部门是否会自动加入该群
     */
    public void setAutoAddUser(Boolean autoAddUser) {
        this.autoAddUser = autoAddUser;
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
     * 获取扩展字段
     *
     * @return ext - 扩展字段
     */
    public String getExt() {
        return ext;
    }

    /**
     * 设置扩展字段
     *
     * @param ext 扩展字段
     */
    public void setExt(String ext) {
        this.ext = ext;
    }
}