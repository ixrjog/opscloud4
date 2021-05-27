package com.baiyi.caesar.common.base;

/**
 * @Author baiyi
 * @Date 2020/10/20 1:28 下午
 * @Version 1.0
 */
public enum  GitlabEventType {
    PUSH(0, "push"),
    TAG_PUSH(1, "tag_push"),
    MERGE_REQUEST(2, "merge_request"),
    REPOSITORY_UPDATE(3, "repository_update"),
    PROJECT_CREATE(4, "project_create"),
    PROJECT_DESTROY(5, "project_destroy"),
    PROJECT_RENAME(6, "project_rename"),
    PROJECT_TRANSFER(7, "project_transfer"),
    PROJECT_UPDATE(8, "project_update"),
    USER_ADD_TO_TEAM(9, "user_add_to_team"),
    USER_REMOVE_FROM_TEAM(10, "user_remove_from_team"),
    USER_CREATE(10, "user_create"),
    USER_DESTROY(11, "user_destroy"),
    USER_FAILED_LOGIN(12, "user_failed_login"),
    USER_RENAME(13, "user_rename"),
    KEY_CREATE(14, "key_create"),
    KEY_DESTROY(15, "key_destroy"),
    GROUP_CREATE(16, "group_create"),
    GROUP_DESTROY(17, "group_destroy"),
    GROUP_RENAME(18, "group_rename"),
    USER_ADD_TO_GROUP(19, "user_add_to_group"),
    USER_REMOVE_FROM_GROUP(20, "user_remove_from_group");
    private int code;
    private String desc;

    GitlabEventType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getGitlabEventTypeName(int code) {
        for (GitlabEventType gitlabEventType : GitlabEventType.values()) {
            if (gitlabEventType.getCode() == code) {
                return gitlabEventType.getDesc();
            }
        }
        return "undefined";
    }
}
