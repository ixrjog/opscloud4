package com.baiyi.opscloud.domain.types;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2021/6/21 11:18 上午
 * @Version 1.0
 */
@Getter
public enum DsAssetTypeEnum {

    DEFAULT("DEFAULT"),
    GROUP("GROUP"),
    USER("USER"),
    ECS("ECS"),
    RAM_USER("RAM_USER"),
    RAM_POLICY("RAM_POLICY"),
    RAM_ACCESS_KEY("RAM_ACCESS_KEY"),
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
    ANSIBLE_VERSION("ANSIBLE_VERSION"),
    ANSIBLE_HOSTS("ANSIBLE_HOSTS");

    private String type;

    DsAssetTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
