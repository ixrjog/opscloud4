package com.baiyi.opscloud.common.constants.enums;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2022/5/9 09:30
 * @Version 1.0
 */
@Getter
public enum ServerMonitorStatusEnum {

    /**
     * status	integer	Status and function of the host.
     * <p>
     * Possible values are:
     * 0 - (default) monitored host;
     * 1 - unmonitored host.
     */

    UNMONITORED(1),
    MONITORED(0),
    NOT_CREATED(-1);

    private final int status;

    ServerMonitorStatusEnum(int status) {
        this.status = status;
    }

}