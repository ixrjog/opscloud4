package com.baiyi.caesar.domain.generator.caesar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "datasource_instance_asset")
public class DatasourceInstanceAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 资产父关系
     */
    @Builder.Default
    @Column(name = "parent_id")
    private Integer parentId = 0;

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
    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

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
     * 资产创建时间
     */
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 资产过期时间
     */
    @Column(name = "expired_time")
    private Date expiredTime;

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