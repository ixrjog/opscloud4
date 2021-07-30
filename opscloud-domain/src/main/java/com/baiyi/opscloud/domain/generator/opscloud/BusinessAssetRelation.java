package com.baiyi.opscloud.domain.generator.opscloud;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "business_asset_relation")
public class BusinessAssetRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 业务类型
     */
    @Column(name = "business_type")
    private Integer businessType;

    /**
     * 业务id
     */
    @Column(name = "business_id")
    private Integer businessId;

    /**
     * 资产id
     */
    @Column(name = "datasource_instance_asset_id")
    private Integer datasourceInstanceAssetId;

    /**
     * 资产类型
     */
    @Column(name = "asset_type")
    private String assetType;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}