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
@Table(name = "ser_deploy_task_item")
public class SerDeployTaskItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 任务ID
     */
    @Column(name = "ser_deploy_task_id")
    private Integer serDeployTaskId;

    /**
     * item名称
     */
    @Column(name = "item_name")
    private String itemName;

    /**
     * item key
     */
    @Column(name = "item_key")
    private String itemKey;

    /**
     * item bucketName
     */
    @Column(name = "item_bucket_name")
    private String itemBucketName;

    /**
     * item md5
     */
    @Column(name = "item_md5")
    private String itemMd5;

    /**
     * item 大小
     */
    @Column(name = "item_size")
    private String itemSize;

    /**
     * 发布人
     */
    @Column(name = "reload_username")
    private String reloadUsername;

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