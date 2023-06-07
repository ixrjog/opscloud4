package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
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
     * item下载链接
     */
    @Column(name = "item_url")
    private String itemUrl;

    /**
     * item md5
     */
    @Column(name = "item_md5")
    private String itemMd5;

    /**
     * item 大小
     */
    @Column(name = "item_size")
    private Integer itemSize;

    /**
     * 发布人
     */
    @Column(name = "deploy_username")
    private String deployUsername;

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