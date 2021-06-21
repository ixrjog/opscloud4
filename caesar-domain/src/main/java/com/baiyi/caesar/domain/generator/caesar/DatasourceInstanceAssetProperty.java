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
@Table(name = "datasource_instance_asset_property")
public class DatasourceInstanceAssetProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 资产id
     */
    @Column(name = "datasource_instance_asset_id")
    private Integer datasourceInstanceAssetId;

    /**
     * 属性名称
     */
    private String name;

    /**
     * 属性值
     */
    private String value;

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
}