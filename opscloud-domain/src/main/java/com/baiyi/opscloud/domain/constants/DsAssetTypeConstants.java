package com.baiyi.opscloud.domain.constants;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2021/6/21 11:18 上午
 * @Version 1.0
 */
@Getter
public enum DsAssetTypeConstants {

    /**
     * 资产类型
     */
    GROUP,
    USER,

    ECS,
    RAM_USER,
    RAM_POLICY,
    RAM_ACCESS_KEY,
    ACR_INSTANCE,
    ACR_REPOSITORY,
    ACR_NAMESPACE,

    ALIYUN_DEVOPS_PROJECT,
    ALIYUN_DEVOPS_SPRINT,
    ALIYUN_DEVOPS_WORKITEM,

    ALIYUN_ARMS_TRACE_APP,

    RDS_INSTANCE,
    RDS_DATABASE,
    REDIS_INSTANCE,

    DMS_USER,

    ONS_ROCKETMQ_INSTANCE,
    ONS_ROCKETMQ_TOPIC,
    ONS_ROCKETMQ_GROUP,

    ONS5_INSTANCE,
    ONS5_TOPIC,
    ONS5_GROUP,

    EC2,
    IAM_POLICY,
    IAM_USER,
    IAM_ACCESS_KEY,
    SQS,
    SNS_TOPIC,
    SNS_SUBSCRIPTION,
    AMAZON_DOMAIN,
    ECR_REPOSITORY,

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
    KUBERNETES_INGRESS,
    KUBERNETES_CUSTOM_RESOURCE,
    ISTIO_VIRTUAL_SERVICE,
    ISTIO_DESTINATION_RULE,

    ZABBIX_USER,
    ZABBIX_USER_GROUP,

    JENKINS_COMPUTER,
    JENKINS_TEMPLATE,
    JENKINS_JOB,

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
    DINGTALK_DEPARTMENT,

    CONSUL_SERVICE,

    HUAWEICLOUD_ECS,

    METER_SPHERE_BUILD_HOOK,
    METER_SPHERE_DEPLOY_HOOK,

    APOLLO_APP,
    APOLLO_CLUSTER,
    APOLLO_NAMESPACE,
    APOLLO_INTERCEPT_RELEASE,

    EVENT_BRIDGE_DEPLOY_EVENT

}
