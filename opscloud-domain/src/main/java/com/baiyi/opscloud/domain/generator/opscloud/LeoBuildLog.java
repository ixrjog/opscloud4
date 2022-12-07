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
@Table(name = "leo_build_log")
public class LeoBuildLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 构建ID
     */
    @Column(name = "build_id")
    private Integer buildId;

    /**
     * 日志级别
     */
    @Column(name = "log_level")
    private String logLevel;

    /**
     * 日志类型
     */
    @Column(name = "log_type")
    private String logType;

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
     * 构建日志
     */
    private String log;
}