package com.sdg.cmdb.domain.configCenter.itemEnum;

/**
 * Created by liangjian on 2017/6/2.
 */
public enum AnsibleItemEnum {
    ANSIBLE_BIN("ANSIBLE_BIN", "ansible命令路径"),
    ANSIBLE_HOSTS_PATH("ANSIBLE_HOSTS_PATH", "持续集成分组配置"),
    ANSIBLE_ALL_HOSTS_PATH("ANSIBLE_ALL_HOSTS_PATH", "所有服务器分组配置"),
    ANSIBLE_LOGCLEANUP_INVOKE("ANSIBLE_LOGCLEANUP_INVOKE", "日志清理命令路径"),
    ANSIBLE_UPDATE_TOMCAT_INVOKE("ANSIBLE_UPDATE_TOMCAT_INVOKE", "updateTomcat命令路径"),
    ANSIBLE_ROLLBACK_TOMCAT_INVOKE("ANSIBLE_ROLLBACK_TOMCAT_INVOKE", "rollbackTomcat命令路径"),
    ANSIBLE_INIT_SYSTEM_INVOKE("ANSIBLE_INIT_SYSTEM_INVOKE", "服务器模版初始化命令"),
    ANSIBLE_CREATE_CI_DEPLOY_DIR("ANSIBLE_CREATE_CI_DEPLOY_DIR", "发布服务器目录创建命令"),
    ANSIBLE_GETWAY_GROUP("ANSIBLE_GETWAY_GROUP", "getway服务器分组名称"),
    ANSIBLE_GETWAY_ACCOUNT_PATH("ANSIBLE_GETWAY_ACCOUNT_PATH", "getway授权命令路径"),
    ANSIBLE_GETWAY_ACCOUNT_INVOKE("ANSIBLE_GETWAY_ACCOUNT_INVOKE", "getway批量授权命令"),
    ANSIBLE_TASK_PATH("ANSIBLE_TASK_PATH", "task任务目录"),
    ANSIBLE_TASK_SCRIPTS_PATH("ANSIBLE_TASK_SCRIPTS_PATH", "taskScripts目录");

    private String itemKey;
    private String itemDesc;

    AnsibleItemEnum(String itemKey, String itemDesc) {
        this.itemKey = itemKey;
        this.itemDesc = itemDesc;
    }

    public String getItemKey() {
        return itemKey;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public String getItemDescByKey(String itemKey) {
        for (AnsibleItemEnum itemEnum : AnsibleItemEnum.values()) {
            if (itemEnum.getItemKey().equals(itemKey)) {
                return itemEnum.getItemDesc();
            }
        }
        return null;
    }
}