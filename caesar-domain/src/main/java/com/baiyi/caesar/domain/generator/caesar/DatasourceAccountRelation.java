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
@Table(name = "datasource_account_relation")
public class DatasourceAccountRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 多实例主账户id
     */
    @Column(name = "account_uid")
    private String accountUid;

    /**
     * 账户id
     */
    @Column(name = "datasource_account_id")
    private Integer datasourceAccountId;

    /**
     * 建立关系的目标id
     */
    @Column(name = "target_id")
    private Integer targetId;

    /**
     * 关系类型
     */
    @Column(name = "relation_type")
    private Integer relationType;

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