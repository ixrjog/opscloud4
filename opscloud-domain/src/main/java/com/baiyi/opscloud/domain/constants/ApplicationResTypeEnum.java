package com.baiyi.opscloud.domain.constants;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2021/9/8 4:15 下午
 * @Version 1.0
 */
@Getter
public enum ApplicationResTypeEnum {

    /**
     * 应用资源
     */
    SERVER,
    SERVERGROUP,
    KUBERNETES_DEPLOYMENT,
    GITLAB_PROJECT,
    GITLAB_GROUP,
    DATASOURCE_INSTANCE

}