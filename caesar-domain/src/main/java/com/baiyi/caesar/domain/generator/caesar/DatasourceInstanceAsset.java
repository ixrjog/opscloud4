package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "datasource_instance_asset")
public class DatasourceInstanceAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 资产父关系
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 数据源实例uuid
     */
    @Column(name = "instance_uuid")
    private String instanceUuid;

    /**
     * 资产名称
     */
    private String name;

    /**
     * 资产id
     */
    @Column(name = "asset_id")
    private String assetId;

    /**
     * 资产类型
     */
    @Column(name = "asset_type")
    private String assetType;

    /**
     * 资产分类
     */
    private String kind;

    /**
     * 资产版本
     */
    private String version;

    /**
     * 有效
     */
    @Column(name = "is_active")
    private Boolean isActive;

    /**
     * 资产关键字1
     */
    @Column(name = "asset_key")
    private String assetKey;

    /**
     * 资产关键字2
     */
    @Column(name = "asset_key_2")
    private String assetKey2;

    /**
     * 区域
     */
    private String zone;

    /**
     * 地区id
     */
    @Column(name = "region_id")
    private String regionId;

    /**
     * 资产状态
     */
    @Column(name = "asset_status")
    private String assetStatus;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    private String description;
}