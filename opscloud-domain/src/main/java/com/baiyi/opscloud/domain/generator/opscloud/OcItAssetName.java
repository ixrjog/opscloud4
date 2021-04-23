package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_it_asset_name")
public class OcItAssetName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 资产类型id
     */
    @Column(name = "asset_type_id")
    private Integer assetTypeId;

    @Column(name = "asset_name")
    private String assetName;

    /**
     * 备注
     */
    private String ramark;

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
     * 获取资产类型id
     *
     * @return asset_type_id - 资产类型id
     */
    public Integer getAssetTypeId() {
        return assetTypeId;
    }

    /**
     * 设置资产类型id
     *
     * @param assetTypeId 资产类型id
     */
    public void setAssetTypeId(Integer assetTypeId) {
        this.assetTypeId = assetTypeId;
    }

    /**
     * @return asset_name
     */
    public String getAssetName() {
        return assetName;
    }

    /**
     * @param assetName
     */
    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    /**
     * 获取备注
     *
     * @return ramark - 备注
     */
    public String getRamark() {
        return ramark;
    }

    /**
     * 设置备注
     *
     * @param ramark 备注
     */
    public void setRamark(String ramark) {
        this.ramark = ramark;
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