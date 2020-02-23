package com.baiyi.opscloud.domain;

import lombok.Getter;


@Getter
public enum ErrorEnum {
    OK(0, "成功", 1),

    // ----------------------- 系统级错误 -----------------------
    SYSTEM_ERROR(10001, "系统错误"),
    // 权限
    AUTHENTICATION_FAILUER(20001,"鉴权失败"),
    AUTHENTICATION_RESOURCE_NOT_EXIST(20002,"资源路径不存在"),
    AUTHENTICATION_REQUEST_NO_TOKEN(20003,"请求中未携带有效令牌Token"),
    AUTHENTICATION_TOKEN_INVALID(20004,"令牌Token无效"),

    // auth
    AUTH_ROLE_HAS_USED(11001, "用户角色正在使用！"),
    AUTH_ROLE_NOT_EXIST(11002, "用户角色配置不存在！"),
    AUTH_RESOURCE_HAS_USED(11003, "资源正在使用！"),
    AUTH_RESOURCE_NOT_EXIST(11004, "资源配置不存在！"),
    AUTH_GROUP_HAS_USED(11005, "资源组正在使用！"),
    AUTH_GROUP_NOT_EXIST(11006, "资源组配置不存在！"),
    AUTH_USER_ROLE_NOT_EXIST(11007, "用户角色配置不存在！"),

    // user
    USER_PASSWORD_NON_COMPLIANCE_WITH_RULES(12002, "用户密码不合规！"),
    USER_PHONE_NON_COMPLIANCE_WITH_RULES(12002, "用户手机号不合规！"),
    USER_EMAIL_NON_COMPLIANCE_WITH_RULES(12002, "用户邮箱不合规！"),

    // server
    SERVER_NAME_NON_COMPLIANCE_WITH_RULES(12002, "服务器名称不合规！"),
    SERVER_GROUP_NOT_SELECTED(12003, "服务器组未选择！"),

    // serverGrooup
    SERVERGROUP_NAME_ALREADY_EXIST(12001, "服务器组名称已存在！"),
    SERVERGROUP_NAME_NON_COMPLIANCE_WITH_RULES(12002, "服务器组名称不合规！"),
    SERVERGROUP_NOT_EXIST(12003, "服务器组不存在！"),
    SERVERGROUP_HAS_USED(12004, "服务器组正在使用！"),
    // serverGroupType
    SERVERGROUP_TYPE_NAME_ALREADY_EXIST(12001, "服务器组类型名称已存在！"),
    SERVERGROUP_TYPE_NOT_EXIST(12003, "服务器组类型不存在！"),
    SERVERGROUP_TYPE_HAS_USED(12004, "服务器组类型正在使用！"),
    // env
    ENV_NAME_ALREADY_EXIST(12001, "环境名称已存在！"),
    ENV_NOT_EXIST(12003, "环境类型不存在！"),
    ENV_HAS_USED(12004, "环境类型正在使用！"),
    // tag
    TAG_KEY_ALREADY_EXIST(12001, "标签key已存在！"),
    TAG_NOT_EXIST(12003, "标签不存在！"),
    TAG_HAS_USED(12004, "标签正在使用！"),

    // cloudserver
    CLOUDSERVER_POWER_MGMT_FAILED(30001,"云主机启停失败"),
    CLOUDSERVER_NOT_EXIST(30002, "云主机不存在！"),

    // ----------------------- 例子 -----------------------
    GET_CONNECTION_ERROR(10002, "获取链接失败！"),
    CREATE_TABLE_ERROR(10003, "创建表失败！"),
    INVOKE_QUERY_ERROR(10004, "执行查询失败！"),
    ROW_TIME_FORMAT_ERROR(10005, "行数据时间格式转换失败！"),

    BINLOG_CONFIG_NOT_EXIST(10101, "binlog配置不存在！"),
    BINLOG_CONFIG_HAS_USED(10102, "binlog配置正在使用！"),
    BINLOG_CONFIG_TOPIC_TABLE_NOT_EXIST(10103, "binlog的topic&table配置不存在！"),

    BUSINESS_DETAIL_NOT_EXIST(10201, "业务域不存在！"),
    BUSINESS_DETAIL_HAS_USED(10202, "业务域正在使用！"),

    RULE_DETAIL_NOT_EXIST(10301, "规则SQL不存在！"),
    RULE_DETAIL_HAS_USED(10302, "规则SQL正在使用！"),

    BUSINESS_RULE_NOT_EXIST(10401, "业务规则不存在！"),
    BUSINESS_RULE_HAS_USED(10402, "业务规则正在使用！"),
    BUSINESS_RULE_UNIQUE_KEY_NOT_EXIST(10403, "业务规则依赖的唯一键未指定！"),
    BUSINESS_RULE_EXTEND_PARAM_ERROR(10404, "业务规则中的扩展参数信息不完善！"),
    BUSINESS_RULE_SQL_RULE_EXIST(10405, "业务规则组队的SQL规则已存在！"),
    BUSINESS_RULE_QUERY_MODE_CHECK_TYPE_NO_MATCH(10406, "业务规则的查询模式为1:n or n:1时，校验类型必须为正确性校验！"),
    BUSINESS_RULE_HAS_USED_RULE_DETAIL(10407, "有业务规则正在使用当前操作的规则SQL，请先停用！"),
    BUSINESS_RULE_TIMEOUT_MAX_ERROR(10408, "业务规则的超时过多，小时单位最多2小时；分钟单位最多60分钟！")
    ;

    private int code;
    private String message;

    /**
     * app需要的状态
     */
    private int appResultStatus;

    ErrorEnum(int code, String message) {
        this(code, message, 0);
    }

    ErrorEnum(int code, String message, int appResultStatus) {
        this.code = code;
        this.message = message;
        this.appResultStatus = appResultStatus;
    }

}
