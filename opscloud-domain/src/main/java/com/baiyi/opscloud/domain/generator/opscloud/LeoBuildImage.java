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
@Table(name = "leo_build_image")
public class LeoBuildImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 构建ID
     */
    @Column(name = "build_id")
    private Integer buildId;

    /**
     * 任务ID
     */
    @Column(name = "job_id")
    private Integer jobId;

    /**
     * 镜像
     */
    private String image;

    /**
     * 版本
     */
    @Column(name = "version_name")
    private String versionName;

    /**
     * 版本描述
     */
    @Column(name = "version_desc")
    private String versionDesc;

    /**
     * 有效
     */
    @Column(name = "is_active")
    private Boolean isActive;

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