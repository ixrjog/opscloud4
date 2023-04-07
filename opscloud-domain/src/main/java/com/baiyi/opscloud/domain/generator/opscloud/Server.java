package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Server implements Serializable {
    @Serial
    private static final long serialVersionUID = -912214650070659359L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 服务器名称
     */
    private String name;

    /**
     * 显示名称
     */
    @Column(name = "display_name")
    private String displayName;

    /**
     * 服务器组id
     */
    @Column(name = "server_group_id")
    private Integer serverGroupId;

    /**
     * 系统类型
     */
    @Column(name = "os_type")
    private String osType;

    /**
     * 环境类型
     */
    @Column(name = "env_type")
    private Integer envType;

    /**
     * 公网ip
     */
    @Column(name = "public_ip")
    private String publicIp;

    /**
     * 私网ip
     */
    @Column(name = "private_ip")
    private String privateIp;

    /**
     * 服务器类型
     */
    @Column(name = "server_type")
    private Integer serverType;

    /**
     * 地区
     */
    private String area;

    /**
     * 序号
     */
    @Column(name = "serial_number")
    private Integer serialNumber;

    /**
     * 监控状态
     */
    @Column(name = "monitor_status")
    private Integer monitorStatus;

    /**
     * 有效
     */
    @Column(name = "is_active")
    private Boolean isActive;

    /**
     * 服务器状态
     */
    @Column(name = "server_status")
    private Integer serverStatus;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 说明
     */
    private String comment;
}