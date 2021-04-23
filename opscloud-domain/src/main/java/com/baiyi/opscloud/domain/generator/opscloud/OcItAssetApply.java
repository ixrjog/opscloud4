package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_it_asset_apply")
public class OcItAssetApply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 资产id
     */
    @Column(name = "asset_id")
    private Integer assetId;

    /**
     * 申领用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 用户所在一级部门id
     */
    @Column(name = "user_org_dept_id")
    private Integer userOrgDeptId;

    /**
     * 申领方式
     * 1:使用
     * 2:借用
     */
    @Column(name = "apply_type")
    private Integer applyType;

    @Column(name = "apply_time")
    private Date applyTime;

    /**
     * 预计归还时间
     */
    @Column(name = "expect_return_time")
    private Date expectReturnTime;

    @Column(name = "return_time")
    private Date returnTime;

    /**
     * 是否归还
     */
    @Column(name = "is_return")
    private Boolean isReturn;

    @Column(name = "create_time",insertable = false,updatable = false)
    private Date createTime;

    @Column(name = "update_time",insertable = false,updatable = false)
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
     * 获取资产id
     *
     * @return asset_id - 资产id
     */
    public Integer getAssetId() {
        return assetId;
    }

    /**
     * 设置资产id
     *
     * @param assetId 资产id
     */
    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    /**
     * 获取申领用户id
     *
     * @return user_id - 申领用户id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置申领用户id
     *
     * @param userId 申领用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取用户所在一级部门id
     *
     * @return user_org_dept_id - 用户所在一级部门id
     */
    public Integer getUserOrgDeptId() {
        return userOrgDeptId;
    }

    /**
     * 设置用户所在一级部门id
     *
     * @param userOrgDeptId 用户所在一级部门id
     */
    public void setUserOrgDeptId(Integer userOrgDeptId) {
        this.userOrgDeptId = userOrgDeptId;
    }

    /**
     * 获取申领方式
     * 1:使用
     * 2:借用
     *
     * @return apply_type - 申领方式
     * 1:使用
     * 2:借用
     */
    public Integer getApplyType() {
        return applyType;
    }

    /**
     * 设置申领方式
     * 1:使用
     * 2:借用
     *
     * @param applyType 申领方式
     *                  1:使用
     *                  2:借用
     */
    public void setApplyType(Integer applyType) {
        this.applyType = applyType;
    }

    /**
     * @return apply_time
     */
    public Date getApplyTime() {
        return applyTime;
    }

    /**
     * @param applyTime
     */
    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    /**
     * 获取预计归还时间
     *
     * @return expect_return_time - 预计归还时间
     */
    public Date getExpectReturnTime() {
        return expectReturnTime;
    }

    /**
     * 设置预计归还时间
     *
     * @param expectReturnTime 预计归还时间
     */
    public void setExpectReturnTime(Date expectReturnTime) {
        this.expectReturnTime = expectReturnTime;
    }

    /**
     * @return return_time
     */
    public Date getReturnTime() {
        return returnTime;
    }

    /**
     * @param returnTime
     */
    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    /**
     * 获取是否归还
     *
     * @return is_return - 是否归还
     */
    public Boolean getIsReturn() {
        return isReturn;
    }

    /**
     * 设置是否归还
     *
     * @param isReturn 是否归还
     */
    public void setIsReturn(Boolean isReturn) {
        this.isReturn = isReturn;
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