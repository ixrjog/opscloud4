package com.baiyi.opscloud.domain.generator.opscloud;

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
@Table(name = "datasource_instance_asset_relation")
public class DatasourceInstanceAssetRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 实例uuid
     */
    @Column(name = "instance_uuid")
    private String instanceUuid;

    /**
     * 源资产id
     */
    @Column(name = "source_asset_id")
    private Integer sourceAssetId;

    /**
     * 目标资产id
     */
    @Column(name = "target_asset_id")
    private Integer targetAssetId;

    /**
     * 关系类型
     */
    @Column(name = "relation_type")
    private String relationType;

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
}