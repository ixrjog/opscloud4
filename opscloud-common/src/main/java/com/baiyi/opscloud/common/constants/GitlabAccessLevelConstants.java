package com.baiyi.opscloud.common.constants;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2022/6/27 15:18
 * @Version 1.0
 */
@Getter
public enum GitlabAccessLevelConstants {

//    GUEST("Guest", 10),
    REPORTER("Reporter", 20),
    DEVELOPER("Developer", 30),
    MASTER("Master", 40),
    OWNER("Owner", 50);

    @Getter
    private final String role;

    private final int accessValue;

    GitlabAccessLevelConstants(String role, int accessValue) {
        this.role = role;
        this.accessValue = accessValue;
    }


}
