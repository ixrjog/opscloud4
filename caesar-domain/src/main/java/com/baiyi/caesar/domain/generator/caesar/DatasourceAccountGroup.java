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
@Table(name = "datasource_account_group")
public class DatasourceAccountGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 多实例主账户id
     */
    @Column(name = "account_uid")
    private String accountUid;

    /**
     * id
     */
    @Column(name = "account_id")
    private String accountId;

    /**
     * 账户类型
     */
    @Column(name = "account_type")
    private Integer accountType;

    /**
     * 组名
     */
    private String name;

    /**
     * 显示名称
     */
    @Column(name = "display_name")
    private String displayName;

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

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

    private String comment;
}