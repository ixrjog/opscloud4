package com.baiyi.opscloud.domain;

import lombok.Getter;


@Getter
public enum ErrorEnum {
    OK(0, "成功", 1),

    // ----------------------- 系统级错误 -----------------------
    SYSTEM_ERROR(10001, "系统错误！"),
    // 权限
    AUTHENTICATION_FAILUER(20001,"鉴权失败！"),
    AUTHENTICATION_API_FAILUER(401,"Api鉴权失败！"),
    AUTHENTICATION_RESOURCE_NOT_EXIST(20002,"资源路径不存在！"),
    AUTHENTICATION_REQUEST_NO_TOKEN(20003,"请求中未携带有效令牌Token！"),
    AUTHENTICATION_TOKEN_INVALID(401,"令牌Token无效！"),
    AUTHENTICATION_API_TOKEN_INVALID(401,"Api令牌Token无效！"),
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
    USER_NOT_EXIST(12002, "用户不存在！"),
    USER_IS_UNACTIVE(12002, "用户被禁用不能修改信息！"),
    // Account is disabled
    ACCOUNT_IS_DISABLE(12002, "账户被禁用！"),
    //applyUserApiToken
    USER_APPLY_API_TOKEN_COMMENT_IS_NULL(12003,"申请ApiToken描述不能为空"),
    USER_APPLY_API_TOKEN_EXPIRED_TIME_FORMAT_ERROR(12003,"申请ApiToken过期时间为空或格式错误"),
    USER_CREDENTIAL_TYPE_ERROR(12003,"用户凭据类型为空或类型错误"),
    USER_CREDENTIAL_ERROR(12003,"用户凭据为空或凭据格式错误!"),

    // resignationUser
    USER_RESIGNATION_ERROR(12003,"用户离职账户禁用错误!"),

    // UserPermission
    USER_PERMISSION_EXIST(12003,"用户授权已存在!"),


    // userGroup
    USERGROUP_NAME_ALREADY_EXIST(12001, "用户组名称已存在！"),
    USERGROUP_NAME_NON_COMPLIANCE_WITH_RULES(12002, "用户组名称不合规！"),


    USER_GRANT_USERGROUP_ERROR(12002, "用户授权用户组错误！"),
    USER_REVOKE_USERGROUP_ERROR(12002, "用户撤销用户组授权错误！"),
    USER_GRANT_SERVERGROUP_ERROR(12002, "用户授权服务器组错误！"),
    USER_REVOKE_SERVERGROUP_ERROR(12002, "用户撤销服务器组授权错误！"),
    // server
    SERVER_NAME_NON_COMPLIANCE_WITH_RULES(12002, "服务器名称不合规！"),
    SERVER_GROUP_NOT_SELECTED(12003, "服务器组未选择！"),
    SERVER_PRIVATE_IP_IS_NAME(12002, "服务器私有Ip不能为空！"),
    SERVER_PRIVATE_IP_CONFLICT(12002, "服务器私有Ip冲突！"),
    SERVER_NOT_EXIST(12002, "服务器不存在"),
    SERVER_DEL_FAIL(12003,"删除服务器失败"),

    // server task
    SERVER_TASK_TREE_NOT_EXIST(11007, "用户服务器资源树不存在或过期！"),
    SERVER_TASK_NOT_EXIST(11007, "服务器任务不存在！"),
    SERVER_TASK_MEMBER_NOT_EXIST(11007, "服务器子任务不存在！"),
    SERVER_TASK_HAS_FINALIIZED_AND_CANNOT_BE_MODIFIED(11007, "任务已经结束！"),
    SERVER_TASK_TIMEOUT(11007, "任务超时！"),
    // serverGrooup
    SERVERGROUP_NAME_ALREADY_EXIST(12001, "服务器组名称已存在！"),
    SERVERGROUP_NAME_NON_COMPLIANCE_WITH_RULES(12002, "服务器组名称不合规！"),
    SERVERGROUP_NOT_EXIST(12003, "服务器组不存在！"),
    SERVERGROUP_ID_EMPTY(12003, "服务器组Id为空！"),
    SERVERGROUP_PROPERTY_ENV_TYPE_EMPTY(12003, "服务器组扩展属性环境为空！"),
    SERVERGROUP_PROPERTY_KV_EMPTY(12003, "服务器组扩展属性键/值为空！"),
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
    CLOUD_SERVER_DELETE_FAIL(30003, "云主机释放失败"),

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

    // Executor
    EXECUTOR_PARAM_TYPE_ERROR(50001, "执行参数类型错误"),

    // Org Department
    ORG_DEPARTMENT_CANNOT_DELETE_ROOT(60001, "不能删除根部门"),
    ORG_DEPARTMENT_CANNOT_DROP_ROOT(60001, "不能拖拽根部门"),
    ORG_DEPARTMENT_CANNOT_JOIN_ROOT(60001, "不能加入根部门"),
    ORG_DEPARTMENT_NOT_EXIST(60001, "部门不存在"),
    ORG_DEPARTMENT_DROP_ERROR(60001, "拖拽类型错误"),
    ORG_DEPARTMENT_MEMBER_ALREADY_EXISTS(60001, "部门成员已存在"),
    ORG_DEPARTMENT_MEMBER_NOT_EXIST(60001, "部门成员不存在"),
    ORG_DEPARTMENT_SUB_DEPT_EXISTS(60001,"有子部门存在"),
    ORG_DEPARTMENT_MEMBER_IS_NOT_EMPTY(60001, "部门成员不为空"),
    ORG_DEPARTMENT_MEMBER_DELETE_ERROR(60001, "删除部门成员错误"),
    ORG_DEPARTMENT_MEMBER_IS_LEADER(60001, "经理不能随意变更部门"),
    ORG_DEPARTMENT_MEMBER_IS_APPROVAL(60001, "拥有审批权不能随意变更部门"),
    ORG_DEPARTMENT_USER_NOT_IN_THE_ORG(60001, "你未加入组织架构"),
    ORG_DEPARTMENT_USER_NOT_IN_THE_DEPT(0, "你未加入部门"),
    ORG_DEPARTMENT_USER_NO_APPROVAL_REATIONSHIP_FOUND(60001, "用户没有建立上级审批关系"),

    // Workorder
    WORKORDER_TICKET_PHASE_ERROR(70001, "工单阶段不正确"),
    WORKORDER_TICKET_NOT_THE_CURRENT_APPROVER(70001, "不是当前审批人"),
    WORKORDER_TICKET_ENTRIES_EXISTS(70001, "工单条目未填写"),

    KEYBOX_PUBLIC_KEY_IS_EMPTY(60001, "公钥不能为空！"),
    KEYBOX_PRIVATE_KEY_IS_EMPTY(60001, "私钥不能为空！"),
    KEYBOX_PASSPHRASE_IS_EMPTY(60001, "密码不能为空！"),

    SERVER_CHANGE_TASK_RUNNING(60001, "当前服务器变更任务执行中！"),
    SERVER_CHANGE_TASK_RESUBMISSION(60001, "当前服务器变更任务重复提交！"),
    SERVER_CHANGE_TASK_GROUP_ID_INCORRECT(60001, "服务器组id不正确！"),

    KUBERNETES_NAMESPACE_NOT_EXIST(70001, "Kubernetes命名空间不存在！"),
    KUBERNETES_CLUSTER_NOT_EXIST(70001, "Kubernetes集群不存在！"),
    KUBERNETES_CREATE_DEPLOYMENT_ERROR(70001, "Kubernetes创建Deployment错误！"),
    KUBERNETES_DELETE_DEPLOYMENT_ERROR(70001, "Kubernetes删除Deployment错误！"),
    KUBERNETES_CREATE_SERVICE_ERROR(70001, "Kubernetes创建Service错误！"),
    KUBERNETES_DELETE_SERVICE_ERROR(70001, "Kubernetes删除Service错误！"),
    KUBERNETES_DELETE_APPLICATION_ERROR(70001, "Kubernetes删除应用错误！"),
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
