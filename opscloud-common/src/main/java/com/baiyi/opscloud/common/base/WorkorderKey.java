package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/4/28 10:59 上午
 * @Version 1.0
 */
public enum WorkorderKey {

    SERVER_GROUP("SERVER_GROUP"),
    AUTH_ROLE("AUTH_ROLE"),
    USER_GROUP("USER_GROUP"),
    RAM_POLICY("RAM_POLICY"),
    ALIYUN_ONS_TOPIC("ALIYUN_ONS_TOPIC"),
    ALIYUN_ONS_GROUP("ALIYUN_ONS_GROUP"),
    KAFKA_GROUP("KAFKA_GROUP"),
    KAFKA_TOPIC("KAFKA_TOPIC"),
    GITLAB_GROUP_MEMBER("GITLAB_GROUP_MEMBER"),
    ;

    private String key;

    WorkorderKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
