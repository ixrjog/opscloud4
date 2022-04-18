package com.baiyi.opscloud.domain.constants;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2021/6/21 11:18 上午
 * @Version 1.0
 */
@Getter
public enum DsAssetTypeConstants {

    GROUP,
    USER,

    ECS,
    RAM_USER,
    RAM_POLICY,
    RAM_ACCESS_KEY,

    RDS_INSTANCE,
    RDS_DATABASE,
    REDIS_INSTANCE,

    DMS_USER,

    ONS_ROCKETMQ_INSTANCE,
    ONS_ROCKETMQ_TOPIC,
    ONS_ROCKETMQ_GROUP,

    EC2,
    IAM_POLICY,
    IAM_USER,
    IAM_ACCESS_KEY,
    SQS,
    SNS_TOPIC,
    SNS_SUBSCRIPTION,
    AMAZON_DOMAIN,

    GITLAB_USER,
    GITLAB_GROUP,
    GITLAB_PROJECT,
    GITLAB_SSHKEY,

    VPC,
    V_SWITCH,
    ECS_IMAGE,
    ECS_SG,
    ALIYUN_DOMAIN,

    KUBERNETES_NAMESPACE,
    KUBERNETES_NODE,
    KUBERNETES_POD,
    KUBERNETES_DEPLOYMENT,
    KUBERNETES_SERVICE,

    ZABBIX_USER,
    ZABBIX_USER_GROUP,

    JENKINS_COMPUTER,

    ZABBIX_HOST,
    ZABBIX_HOST_GROUP,
    ZABBIX_TEMPLATE,
    ZABBIX_TRIGGER,
    ZABBIX_PROBLEM,

    ANSIBLE_VERSION,
    ANSIBLE_HOSTS,

    TENCENT_EXMAIL_USER,

    NEXUS_ASSET,

    SONAR_PROJECT,
    NACOS_CLUSTER_NODE,
    NACOS_PERMISSION,
    NACOS_USER,
    NACOS_ROLE,

    DINGTALK_USER,
    DINGTALK_DEPARTMENT;

//    private final String type;
//
//    DsAssetTypeEnum(String type) {
//        this.type = type;
//    }
//
//    public String getType() {
//        return this.type;
//    }
}
