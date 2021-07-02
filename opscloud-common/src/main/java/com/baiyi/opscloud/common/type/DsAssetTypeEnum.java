package com.baiyi.opscloud.common.type;

/**
 * @Author baiyi
 * @Date 2021/6/21 11:18 上午
 * @Version 1.0
 */
public enum DsAssetTypeEnum {

    GROUP("GROUP"),
    USER("USER"),
    ECS("ECS"),
    RAM_USER("RAM_USER"),
    RAM_POLICY("RAM_POLICY"),
    GITLAB_USER("GITLAB_USER"),
    GITLAB_GROUP("GITLAB_GROUP"),
    GITLAB_PROJECT("GITLAB_PROJECT"),
    GITLAB_SSHKEY("GITLAB_SSHKEY"),
    VPC("VPC"),
    V_SWITCH("V_SWITCH"),
    ECS_IMAGE("ECS_IMAGE"),
    ECS_SG("ECS_SG"),
    KUBERNETES_NAMESPACE("KUBERNETES_NAMESPACE"),
    KUBERNETES_POD("KUBERNETES_POD"),
    KUBERNETES_DEPLOYMENT("KUBERNETES_DEPLOYMENT"),
    ZABBIX_USER("ZABBIX_USER"),
    ZABBIX_USER_GROUP("ZABBIX_USER_GROUP"),
    JENKINS_COMPUTER("JENKINS_COMPUTER"),
    ZABBIX_HOST("ZABBIX_HOST"),
    ZABBIX_HOST_GROUP("ZABBIX_HOST_GROUP"),
    ZABBIX_TEMPLATE("ZABBIX_TEMPLATE"),
    ZABBIX_TRIGGER("ZABBIX_TRIGGER"),
    ZABBIX_PROBLEM("ZABBIX_PROBLEM"),
    ;

    private String type;

    DsAssetTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
