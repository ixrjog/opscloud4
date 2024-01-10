package com.baiyi.opscloud.domain.constants;

import lombok.Getter;

import java.util.Arrays;

/**
 * @Author baiyi
 * @Date 2022/12/8 11:30
 * @Version 1.0
 */
@Getter
public enum DeployTypeConstants {

    /**
     *
     */
    ROLLING("滚动 Rolling"),
    REDEPLOY("重启 Redeploy"),
    OFFLINE("下线 Offline"),
    // GRAY("灰度 Gray"),
    BLUEGREEN("蓝绿 Blue/Green");

    private final String desc;

    DeployTypeConstants(String desc) {
        this.desc = desc;
    }

    public static String getDesc(String name) {
        return Arrays.stream(DeployTypeConstants.values()).filter(typeEnum -> typeEnum.name().equals(name)).findFirst().map(DeployTypeConstants::getDesc).orElse("undefined");
    }

}