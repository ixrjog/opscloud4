package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_it_asset")
public class OcItAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 资产所属公司id
     */
    @Column(name = "asset_company")
    private Integer assetCompany;

    /**
     * 资产编码
     */
    @Column(name = "asset_code")
    private String assetCode;

    /**
     * 资产名称id
     */
    @Column(name = "asset_name_id")
    private Integer assetNameId;

    /**
     * 资产状态\\n1:空闲\\n2:在用\\n3:借用|n4:报废
     */
    @Column(name = "asset_status")
    private Integer assetStatus;

    /**
     * 资产配置
     */
    @Column(name = "asset_configuration")
    private String assetConfiguration;

    /**
     * 购置金额
     */
    @Column(name = "asset_price")
    private String assetPrice;

    /**
     * 领用/借用日期
     */
    @Column(name = "use_time")
    private Date useTime;

    /**
     * 购置/起租日期
     */
    @Column(name = "asset_add_time")
    private Date assetAddTime;

    /**
     * 资产放置地点
     */
    @Column(name = "asset_place")
    private String assetPlace;

    /**
     * 备注
     */
    private String remark;

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
     * 获取资产所属公司id
     *
     * @return asset_company - 资产所属公司id
     */
    public Integer getAssetCompany() {
        return assetCompany;
    }

    /**
     * 设置资产所属公司id
     *
     * @param assetCompany 资产所属公司id
     */
    public void setAssetCompany(Integer assetCompany) {
        this.assetCompany = assetCompany;
    }

    /**
     * 获取资产编码
     *
     * @return asset_code - 资产编码
     */
    public String getAssetCode() {
        return assetCode;
    }

    /**
     * 设置资产编码
     *
     * @param assetCode 资产编码
     */
    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    /**
     * 获取资产名称id
     *
     * @return asset_name_id - 资产名称id
     */
    public Integer getAssetNameId() {
        return assetNameId;
    }

    /**
     * 设置资产名称id
     *
     * @param assetNameId 资产名称id
     */
    public void setAssetNameId(Integer assetNameId) {
        this.assetNameId = assetNameId;
    }

    /**
     * 获取资产状态\\n1:空闲\\n2:在用\\n3:借用|n4:报废
     *
     * @return asset_status - 资产状态\\n1:空闲\\n2:在用\\n3:借用|n4:报废
     */
    public Integer getAssetStatus() {
        return assetStatus;
    }

    /**
     * 设置资产状态\\n1:空闲\\n2:在用\\n3:借用|n4:报废
     *
     * @param assetStatus 资产状态\\n1:空闲\\n2:在用\\n3:借用|n4:报废
     */
    public void setAssetStatus(Integer assetStatus) {
        this.assetStatus = assetStatus;
    }

    /**
     * 获取资产配置
     *
     * @return asset_configuration - 资产配置
     */
    public String getAssetConfiguration() {
        return assetConfiguration;
    }

    /**
     * 设置资产配置
     *
     * @param assetConfiguration 资产配置
     */
    public void setAssetConfiguration(String assetConfiguration) {
        this.assetConfiguration = assetConfiguration;
    }

    /**
     * 获取购置金额
     *
     * @return asset_price - 购置金额
     */
    public String getAssetPrice() {
        return assetPrice;
    }

    /**
     * 设置购置金额
     *
     * @param assetPrice 购置金额
     */
    public void setAssetPrice(String assetPrice) {
        this.assetPrice = assetPrice;
    }

    /**
     * 获取领用/借用日期
     *
     * @return use_time - 领用/借用日期
     */
    public Date getUseTime() {
        return useTime;
    }

    /**
     * 设置领用/借用日期
     *
     * @param useTime 领用/借用日期
     */
    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    /**
     * 获取购置/起租日期
     *
     * @return asset_add_time - 购置/起租日期
     */
    public Date getAssetAddTime() {
        return assetAddTime;
    }

    /**
     * 设置购置/起租日期
     *
     * @param assetAddTime 购置/起租日期
     */
    public void setAssetAddTime(Date assetAddTime) {
        this.assetAddTime = assetAddTime;
    }

    /**
     * 获取资产放置地点
     *
     * @return asset_place - 资产放置地点
     */
    public String getAssetPlace() {
        return assetPlace;
    }

    /**
     * 设置资产放置地点
     *
     * @param assetPlace 资产放置地点
     */
    public void setAssetPlace(String assetPlace) {
        this.assetPlace = assetPlace;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
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