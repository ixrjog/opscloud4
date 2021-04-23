package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_it_asset_dispose")
public class OcItAssetDispose {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 资产id
     */
    @Column(name = "asset_id")
    private Integer assetId;

    /**
     * 1:退租\n2:报废清理\n3:盘亏处理\n4:转让出售
     */
    @Column(name = "dispose_type")
    private Integer disposeType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 处置时间
     */
    @Column(name = "dispose_time")
    private Date disposeTime;

    /**
     * 拓展字段
     */
    private String expand;

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
     * 获取1:退租\n2:报废清理\n3:盘亏处理\n4:转让出售
     *
     * @return dispose_type - 1:退租\n2:报废清理\n3:盘亏处理\n4:转让出售
     */
    public Integer getDisposeType() {
        return disposeType;
    }

    /**
     * 设置1:退租\n2:报废清理\n3:盘亏处理\n4:转让出售
     *
     * @param disposeType 1:退租\n2:报废清理\n3:盘亏处理\n4:转让出售
     */
    public void setDisposeType(Integer disposeType) {
        this.disposeType = disposeType;
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
     * 获取处置时间
     *
     * @return dispose_time - 处置时间
     */
    public Date getDisposeTime() {
        return disposeTime;
    }

    /**
     * 设置处置时间
     *
     * @param disposeTime 处置时间
     */
    public void setDisposeTime(Date disposeTime) {
        this.disposeTime = disposeTime;
    }

    /**
     * 获取拓展字段
     *
     * @return expand - 拓展字段
     */
    public String getExpand() {
        return expand;
    }

    /**
     * 设置拓展字段
     *
     * @param expand 拓展字段
     */
    public void setExpand(String expand) {
        this.expand = expand;
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