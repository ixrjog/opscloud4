package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "datasource_instance_asset_subscription")
public class DatasourceInstanceAssetSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 数据源实例uuid
     */
    @Column(name = "instance_uuid")
    private String instanceUuid;

    /**
     * 资产id
     */
    @Column(name = "datasource_instance_asset_id")
    private Integer datasourceInstanceAssetId;

    /**
     * 有效
     */
    @Column(name = "is_active")
    private Boolean isActive;

    /**
     * 最后订阅时间
     */
    @Column(name = "last_subscription_time")
    private Date lastSubscriptionTime;

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

    /**
     * 订阅剧本
     */
    private String playbook;

    /**
     * 外部变量
     */
    private String vars;

    /**
     * 最后订阅日志
     */
    @Column(name = "last_subscription_log")
    private String lastSubscriptionLog;

    private String comment;
}