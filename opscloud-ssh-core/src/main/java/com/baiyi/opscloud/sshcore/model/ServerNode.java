package com.baiyi.opscloud.sshcore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/5/28 5:58 下午
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerNode {
    // 实例id
    private String instanceId;
    // 标签（服务器名+ip）
    private String label;

    private Integer id;
    private String name;
    private Integer serverGroupId;
    private String osType;
    private Integer envType;
    private String publicIp;
    private String privateIp;
    private Integer serverType;
    private String area;
    private Integer serialNumber;
    private Integer monitorStatus;
    private Boolean isActive;
    private Integer serverStatus;
    private String comment;

}
