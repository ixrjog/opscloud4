package com.baiyi.opscloud.domain.types;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2020/2/22 5:46 下午
 * @Version 1.0
 */
@Getter
public enum BusinessTypeEnum {
    COMMON(0, "通用"),
    SERVER(1, "服务器", true),
    SERVERGROUP(2, "服务器组", true),
    USER(3, "用户"),
    USERGROUP(4, "用户组"),
    ASSET(5, "资产", true),
    // CLOUD_DATABASE(5,"CLOUD_DATABASE"),
    /**
     * 服务器器管理员账户
     **/
    SERVER_ADMINISTRATOR_ACCOUNT(6, "SERVER_ADMINISTRATOR_ACCOUNT"),
    ALIYUN_RAM_ACCOUNT(7, "ALIYUN_RAM_ACCOUNT"),
    /**
     * 应用授权
     **/
    APPLICATION(8, "APPLICATION"),
    /**
     * Gitlab项目
     **/
    GITLAB_PROJECT(9, "GITLAB_PROJECT"),
    //JENKINS_TPL(10,"JENKINS_TPL"),
    DINGTALK(11, "DINGTALK"),
    ALIYUN_OSS_BUCKET(12, "ALIYUN_OSS_BUCKET"),
    /**
     * 构建任务
     **/
    // APPLICATION_BUILD_JOB(13),
    /**
     * 部署任务
     **/
    //APPLICATION_DEPLOYMENT_JOB(14),
    GITLAB_GROUP(15, "GITLAB_GROUP"),
    DATASOURCE_INSTANCE(16, "数据源实例"),
    DATASOURCE_ASSET(17, "数据源实例资产"),
    ;

    private String name;

    private int type;

    private boolean inApplication;

    public static BusinessTypeEnum getByType(int type) {
        for (BusinessTypeEnum typeEnum : BusinessTypeEnum.values())
            if (typeEnum.type == type)
                return typeEnum;
        return null;
    }

    BusinessTypeEnum(int type, String name, boolean inApplication) {
        this.type = type;
        this.name = name;
        this.inApplication = inApplication;
    }

    BusinessTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
        this.inApplication = false;
    }

}
