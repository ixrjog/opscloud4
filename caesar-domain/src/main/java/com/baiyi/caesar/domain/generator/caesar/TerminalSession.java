package com.baiyi.caesar.domain.generator.caesar;

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
@Table(name = "terminal_session")
public class TerminalSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 会话uuid
     */
    @Column(name = "session_id")
    private String sessionId;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    private String username;

    @Column(name = "remote_addr")
    private String remoteAddr;

    /**
     * 会话是否关闭
     */
    @Column(name = "session_closed")
    private Boolean sessionClosed;

    /**
     * 关闭时间
     */
    @Column(name = "close_time")
    private Date closeTime;

    /**
     * 服务端主机名
     */
    @Column(name = "server_hostname")
    private String serverHostname;

    /**
     * 服务端地址
     */
    @Column(name = "server_addr")
    private String serverAddr;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}