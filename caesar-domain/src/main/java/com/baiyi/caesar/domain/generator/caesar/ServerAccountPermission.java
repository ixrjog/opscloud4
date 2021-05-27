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
@Table(name = "server_account_permission")
public class ServerAccountPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 服务器id
     */
    @Column(name = "server_id")
    private Integer serverId;

    /**
     * 账户id
     */
    @Column(name = "server_account_id")
    private Integer serverAccountId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}