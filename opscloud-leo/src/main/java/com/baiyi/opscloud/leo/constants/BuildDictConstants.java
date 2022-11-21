package com.baiyi.opscloud.leo.constants;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2022/11/21 13:31
 * @Version 1.0
 */
public enum BuildDictConstants {


    ENV_NAME("envName"),
    APPLICATION_NAME("applicationName"),
    APPLICATION_TAGS("applicationTags"),
    JOB_NAME("jobName"),
    VERSION_NAME("versionName"),
    BUILD_NUMBER("buildNumber"),
    BRANCH("branch"),
    COMMIT("commit"),
    DISPLAY_NAME("displayName"),
    IMAGE("image"),
    IMAGE_TAG("imageTag"),

    ;


    @Getter
    private final String key;

    BuildDictConstants(String key) {
        this.key = key;
    }
}
