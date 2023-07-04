package com.baiyi.opscloud.factory.gitlab.enums;

/**
 * @Author baiyi
 * @Date 2021/10/29 10:55 上午
 * @Version 1.0
 */
public enum GitLabEventNameEnum {

    /**
     * GitLab 事件
     */
    PROJECT_CREATE,
    PROJECT_DESTROY,
    PROJECT_RENAME,
    KEY_CREATE,
    KEY_DESTROY,

    // Push events
    PUSH,
    // Merge request events
    MERGE_REQUEST

}
