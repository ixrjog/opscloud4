package com.baiyi.opscloud.leo.constants;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2023/4/25 13:31
 * @Version 1.0
 */
@Getter
public enum MavenPublishConstants {

    /**
     * 参数
     */
    ARTIFACT_ID("artifactId"),
    GROUP_ID("groupId"),
    VERSION("version");

    private final String param;

    MavenPublishConstants(String param) {
        this.param = param;
    }

}