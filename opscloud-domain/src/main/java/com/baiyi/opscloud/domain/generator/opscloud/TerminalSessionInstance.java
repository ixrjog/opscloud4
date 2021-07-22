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
@Table(name = "terminal_session_instance")
public class TerminalSessionInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 会话uuid
     */
    @Column(name = "session_id")
    private String sessionId;

    /**
     * 实例id
     */
    @Column(name = "instance_id")
    private String instanceId;

    /**
     * 会话复制实例id
     */
    @Column(name = "duplicate_instance_id")
    private String duplicateInstanceId;


    /**
     * 实例会话类型
     */
    @Column(name = "instance_session_type")
    private String instanceSessionType;

    /**
     * 登录账户
     */
    @Column(name = "login_user")
    private String loginUser;

    /**
     * 主机ip
     */
    @Column(name = "host_ip")
    private String hostIp;

    /**
     * 输出文件大小
     */
    @Column(name = "output_size")
    private Long outputSize;

    /**
     * 是否关闭
     */
    @Column(name = "instance_closed")
    private Boolean instanceClosed;

    /**
     * 打开时间
     */
    @Column(name = "open_time")
    private Date openTime;

    /**
     * 关闭时间
     */
    @Column(name = "close_time")
    private Date closeTime;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}