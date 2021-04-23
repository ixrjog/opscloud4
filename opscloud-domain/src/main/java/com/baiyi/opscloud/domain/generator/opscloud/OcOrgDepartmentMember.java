package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_org_department_member")
public class OcOrgDepartmentMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 部门id
     */
    @Column(name = "department_id")
    private Integer departmentId;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 部门类型
     */
    @Column(name = "member_type")
    private Integer memberType;

    /**
     * 经理
     */
    @Column(name = "is_leader")
    private Integer isLeader;

    /**
     * 审批
     */
    @Column(name = "is_approval_authority")
    private Integer isApprovalAuthority;

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

    /**
     * 中文说明
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
     * 获取部门id
     *
     * @return department_id - 部门id
     */
    public Integer getDepartmentId() {
        return departmentId;
    }

    /**
     * 设置部门id
     *
     * @param departmentId 部门id
     */
    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
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
     * 获取部门名称
     *
     * @return username - 部门名称
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置部门名称
     *
     * @param username 部门名称
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取部门类型
     *
     * @return member_type - 部门类型
     */
    public Integer getMemberType() {
        return memberType;
    }

    /**
     * 设置部门类型
     *
     * @param memberType 部门类型
     */
    public void setMemberType(Integer memberType) {
        this.memberType = memberType;
    }

    /**
     * 获取经理
     *
     * @return is_leader - 经理
     */
    public Integer getIsLeader() {
        return isLeader;
    }

    /**
     * 设置经理
     *
     * @param isLeader 经理
     */
    public void setIsLeader(Integer isLeader) {
        this.isLeader = isLeader;
    }

    /**
     * 获取审批
     *
     * @return is_approval_authority - 审批
     */
    public Integer getIsApprovalAuthority() {
        return isApprovalAuthority;
    }

    /**
     * 设置审批
     *
     * @param isApprovalAuthority 审批
     */
    public void setIsApprovalAuthority(Integer isApprovalAuthority) {
        this.isApprovalAuthority = isApprovalAuthority;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取中文说明
     *
     * @return comment - 中文说明
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置中文说明
     *
     * @param comment 中文说明
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}