package com.baiyi.opscloud.domain;

import lombok.Getter;


@Getter
public enum ErrorEnum {
    OK(0, "成功", 1),

    // ----------------------- 系统级错误 -----------------------
    SYSTEM_ERROR(10001, "系统错误"),
    // 权限
    AUTHENTICATION_FAILUER(20001,"鉴权失败"),
    AUTHENTICATION_API_FAILUER(20001,"Api鉴权失败"),
    AUTHENTICATION_RESOURCE_NOT_EXIST(20002,"资源路径不存在"),
    AUTHENTICATION_REQUEST_NO_TOKEN(20003,"请求中未携带有效令牌Token"),
    AUTHENTICATION_TOKEN_INVALID(20004,"令牌Token无效"),
    AUTHENTICATION_API_TOKEN_INVALID(20004,"Api令牌Token无效"),
    USER_LOGIN_FAILUER(20001,"登录失败请检查用户名或密码是否正确，请重试！"),

    // auth
    AUTH_ROLE_HAS_USED(11001, "用户角色正在使用！"),
    AUTH_ROLE_NOT_EXIST(11002, "用户角色配置不存在！"),
    AUTH_RESOURCE_HAS_USED(11003, "资源正在使用！"),
    AUTH_RESOURCE_NOT_EXIST(11004, "资源配置不存在！"),
    AUTH_GROUP_HAS_USED(11005, "资源组正在使用！"),
    AUTH_GROUP_NOT_EXIST(11006, "资源组配置不存在！"),
    AUTH_USER_ROLE_NOT_EXIST(11007, "用户角色配置不存在！"),

    // user
    USER_USERNAME_IS_NULL(12002, "用户名为空！"),
    USER_PASSWORD_IS_NULL(12002, "用户密码为空！"),
    USER_DISPLAYNAME_IS_NULL(12002, "用户显示名为空！"),
    USER_EMAIL_IS_NULL(12002, "用户显示名为空！"),
    USER_USERNAME_NON_COMPLIANCE_WITH_RULES(12002, "用户名不合规！"),
    USER_PASSWORD_NON_COMPLIANCE_WITH_RULES(12002, "用户密码不合规！"),
    USER_PHONE_NON_COMPLIANCE_WITH_RULES(12002, "用户手机号不合规！"),
    USER_EMAIL_NON_COMPLIANCE_WITH_RULES(12002, "用户邮箱不合规！"),
    //applyUserApiToken
    USER_APPLY_API_TOKEN_COMMENT_IS_NULL(12003,"申请ApiToken描述不能为空"),
    USER_APPLY_API_TOKEN_EXPIRED_TIME_FORMAT_ERROR(12003,"申请ApiToken过期时间为空或格式错误"),
    USER_CREDENTIAL_TYPE_ERROR(12003,"用户凭据类型为空或类型错误"),
    USER_CREDENTIAL_ERROR(12003,"用户凭据为空或凭据格式错误!"),

    // UserPermission
    USER_PERMISSION_EXIST(12003,"用户授权已存在!"),


    // userGroup
    USERGROUP_NAME_ALREADY_EXIST(12001, "用户组名称已存在！"),
    USERGROUP_NAME_NON_COMPLIANCE_WITH_RULES(12002, "用户组名称不合规！"),


    USER_GRANT_USERGROUP_ERROR(12002, "授权用户组错误！"),
    USER_REVOKE_USERGROUP_ERROR(12002, "撤销用户组授权错误！"),

    // server
    SERVER_NAME_NON_COMPLIANCE_WITH_RULES(12002, "服务器名称不合规！"),
    SERVER_GROUP_NOT_SELECTED(12003, "服务器组未选择！"),
    SERVER_PRIVATE_IP_IS_NAME(12002, "服务器私有Ip不能为空！"),
    SERVER_PRIVATE_IP_CONFLICT(12002, "服务器私有Ip冲突！"),
    SERVER_NOT_EXIST(12002, "服务器不存在"),

    // serverGrooup
    SERVERGROUP_NAME_ALREADY_EXIST(12001, "服务器组名称已存在！"),
    SERVERGROUP_NAME_NON_COMPLIANCE_WITH_RULES(12002, "服务器组名称不合规！"),
    SERVERGROUP_NOT_EXIST(12003, "服务器组不存在！"),
    SERVERGROUP_HAS_USED(12004, "服务器组正在使用！"),
    // serverGroupType
    SERVERGROUP_TYPE_NAME_ALREADY_EXIST(12001, "服务器组类型名称已存在！"),
    SERVERGROUP_TYPE_NOT_EXIST(12003, "服务器组类型不存在！"),
    SERVERGROUP_TYPE_HAS_USED(12004, "服务器组类型正在使用！"),
    SERVERGROUP_TYPE_IS_DEFAULT(12005, "不能删除默认服务器组类型！"),
    // env
    ENV_NAME_ALREADY_EXIST(12001, "环境名称已存在！"),
    ENV_NOT_EXIST(12003, "环境类型不存在！"),
    ENV_HAS_USED(12004, "环境类型正在使用！"),
    ENV_IS_DEFAULT(12005, "不能删除默认环境类型！"),
    // tag
    TAG_KEY_ALREADY_EXIST(12001, "标签key已存在！"),
    TAG_NOT_EXIST(12003, "标签不存在！"),
    TAG_HAS_USED(12004, "标签正在使用！"),

    // cloud
    CLOUD_TYPE_IS_NULL(30002, "未指定云类型！"),

    // cloudInstanceTemplate
    CLOUD_INSTANCE_TEMPLATE_NOT_EXIST(30002, "云实例模版不存在！"),
    CLOUD_INSTANCE_TEMPLATE_NAME_NON_COMPLIANCE_WITH_RULES(12002, "云实例模版名称不合规！"),

    // cloudServer
    CLOUD_SERVER_POWER_MGMT_FAILED(30001,"云主机启停失败"),
    CLOUD_SERVER_NOT_EXIST(30002, "云主机不存在！"),

    // cloudDB
    CLOUD_DB_NOT_EXIST(30002, "云数据库实例不存在！"),

    // cloudDBDatbase
    CLOUD_DB_DATABASE_NOT_EXIST(30002, "云数据库不存在！"),

    // cloudImage
    CLOUD_IMAGE_NOT_EXIST(30002, "云镜像不存在！"),

    // cloudVPC
    CLOUD_VPC_NOT_EXIST(30002, "云VPC不存在！"),
    CLOUD_VPC_SECURITY_GROUP_NOT_EXIST(30002, "云VPC安全组不存在！"),
    CLOUD_VPC_VSWITCH_NOT_EXIST(30002, "云VPC虚拟交换机不存在！"),

    // create instance Zone must be selected
    CREATE_CLOUD_INSTANCE_ZONEID_MUST_BE_SELECTED(30002,"必须指定创建实例的可用区"),
    CREATE_CLOUD_INSTANCE_VSWITCHIDS_MUST_BE_SELECTED(30002,"必须指定创建实例的虚拟交换机列表"),

    // aliyunRDSMysql
    ALIYUN_RDS_MYSQL_CREATE_ACCOUNT_ERROR(30002, "创建云数据库账户错误！"),
    ALIYUN_RDS_MYSQL_DESCRIBE_ACCOUNT_ERROR(30002, "查询云数据库账户错误！"),
    ALIYUN_RDS_MYSQL_GRANT_ACCOUNT_PRIVILEGE_ERROR(30002, "云数据库账户授权错误！"),
    ALIYUN_RDS_MYSQL_REVOKE_ACCOUNT_PRIVILEGE_ERROR(30002, "云数据库账户撤销授权错误！"),
    ALIYUN_RDS_MYSQL_DELETE_ACCOUNT_ERROR(30002, "云数据库账户删除错误！"),

    // jumpserver
    JUMPSERVER_ASSETS_NODE_ROOT_NOT_EXIST(40002, "资产根节点不存在(表：assets_node,字段：key = 1)！"),
    JUMPSERVER_ADMINISTRATOR_AUTHORIZATION_CANNOT_BE_REVOKED(40002, "不能撤销Administrator账户管理员授权"),

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
