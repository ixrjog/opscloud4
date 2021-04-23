package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_it_asset_company")
public class OcItAssetCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 资产归属公司查询关键字
     */
    @Column(name = "asset_company_key")
    private String assetCompanyKey;

    /**
     * 资产归属公司名称
     */
    @Column(name = "asset_company_name")
    private String assetCompanyName;

    /**
     * 资产归属公司类型\n1:内部，所对应的购置方式为采购\n2:外部，所对应的购置方式为租赁
     */
    @Column(name = "asset_company_type")
    private Integer assetCompanyType;

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
     * 获取资产归属公司查询关键字
     *
     * @return asset_company_key - 资产归属公司查询关键字
     */
    public String getAssetCompanyKey() {
        return assetCompanyKey;
    }

    /**
     * 设置资产归属公司查询关键字
     *
     * @param assetCompanyKey 资产归属公司查询关键字
     */
    public void setAssetCompanyKey(String assetCompanyKey) {
        this.assetCompanyKey = assetCompanyKey;
    }

    /**
     * 获取资产归属公司名称
     *
     * @return asset_company_name - 资产归属公司名称
     */
    public String getAssetCompanyName() {
        return assetCompanyName;
    }

    /**
     * 设置资产归属公司名称
     *
     * @param assetCompanyName 资产归属公司名称
     */
    public void setAssetCompanyName(String assetCompanyName) {
        this.assetCompanyName = assetCompanyName;
    }

    /**
     * 获取资产归属公司类型\n1:内部，所对应的购置方式为采购\n2:外部，所对应的购置方式为租赁
     *
     * @return asset_company_type - 资产归属公司类型\n1:内部，所对应的购置方式为采购\n2:外部，所对应的购置方式为租赁
     */
    public Integer getAssetCompanyType() {
        return assetCompanyType;
    }

    /**
     * 设置资产归属公司类型\n1:内部，所对应的购置方式为采购\n2:外部，所对应的购置方式为租赁
     *
     * @param assetCompanyType 资产归属公司类型\n1:内部，所对应的购置方式为采购\n2:外部，所对应的购置方式为租赁
     */
    public void setAssetCompanyType(Integer assetCompanyType) {
        this.assetCompanyType = assetCompanyType;
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