package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "business_relation")
public class BusinessRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 源业务类型
     */
    @Column(name = "source_business_type")
    private Integer sourceBusinessType;

    /**
     * 源业务id
     */
    @Column(name = "source_business_id")
    private Integer sourceBusinessId;

    /**
     * 目标业务类型
     */
    @Column(name = "target_business_type")
    private Integer targetBusinessType;

    /**
     * 目标业务id
     */
    @Column(name = "target_business_id")
    private Integer targetBusinessId;

    /**
     * 关系类型（目标业务类型描述）
     */
    @Column(name = "relation_type")
    private String relationType;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}