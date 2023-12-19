package com.baiyi.opscloud.common.constants.enums;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2021/5/25 1:31 下午
 * @Version 1.0
 */
@Getter
public enum ProtocolEnum {

    /**
     * 协议类型
     */
    SSH("ssh"),
    VNC("vnc"),
    RDP("rdp");

    private final String type;

    ProtocolEnum(String type) {
        this.type = type;
    }

}