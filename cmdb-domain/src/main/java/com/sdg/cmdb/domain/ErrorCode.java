package com.sdg.cmdb.domain;

/**
 * Created by zxxiao on 16/9/7.
 */
public enum ErrorCode {
    credentialError("0001", "登录凭证错误!"),
    requestNoToken("0002", "未携带有效凭证"),
    authenticationFailure("0003", "鉴权失败!"),
    tokenInvalid("0004", "凭证过期!"),
    resourceNotExist("0005", "请求资源不存在!"),

    resourceGroupRely("0006", "资源组被使用,不允许删除!"),
    resourceHasRelyCannotDel("0007", "资源有角色使用,不允许删除!"),
    roleHasRelyCannotDel("0008", "角色有用户使用,不允许删除!"),
    ipHasUse("0009", "ip被使用!"),

    configHasChildren("0010", "父类目有子类目,不允许删除!"),

    serverGroupHasServer("0101", "服务器组下还有服务器在使用!"),
    serverGroupHasUse("0102", "服务器组名称被使用!"),
    serverGroupNotExist("0103","服务器组不存在"),
    serverGroupHasUser("0104", "服务器组有堡垒机用户引用!"),
    serverGroupServerNull("0105","服务器组中没有服务器信息"),

    configPropertyHasServer("0012", "属性正在使用!"),
    configPropertyGroupHasChildProperty("0013", "属性组有子属性!"),

    serverHasUse("0014", "服务器在使用中!"),
    userNotExist("0015", "用户不存在!"),
    userPwdNotInput("0016", "用户授权密码未录入!"),



    keyboxFailure("0018", "堡垒机错误!"),

    configFileGroupHasUsed("0019", "文件组正在使用!"),
    configFileNotExist("0020", "文件不存在!"),
    configFileParamNumsError("0021", "配置文件参数不正确!"),

    ecsRefreshFailure("0022", "获取ECS服务列表错误!"),

    zabbixHostCreate("0023","添加监控错误"),
    zabbixHostDelete("0023","删除监控错误"),
    zabbixHostDisable("0024","禁用监控错误"),
    zabbixHostEisable("0025","启用监控错误"),
    zabbixUserDelete("0026","删除用户错误"),
    zabbixUserCreate("0027","添加用户错误"),
    zabbixCIServerGroupNotExist("0028","服务器组不存在"),
    zabbixCIKeyCheckFailure("0030","key校验失败"),

    wsServerGroupParamError("0031", "WS服务器组参数错误!"),
    wsServerGroupRepeatSub("0032", "WS服务器组重复订阅!"),

    explainNotExist("0034", "订阅计划不存在!"),

    serverNotExist("0035", "服务器不存在!"),

    logcleanupTimeError("0036", "日志清理未到时间!"),
    logcleanupDisabled("0037", "日志清理被禁用!"),



    ecsCreateServerExist("0039", "已经有服务器，请使用扩容!"),

    userExist("0040", "用户存在!"),

    ecsStatusError("0041", "ecs运行状态不正确（请在STOPPED或RUNNING状态下重试）!"),

    serverGroupAuthed("0042", "服务器组已授权!"),

    serverGroupRepeat("0043", "服务器组重复添加!"),

    todoStatusIsNotEstablish("0044", "此工单已经提交!"),

    usernameIsNull("0044", "用户名不存在!"),

    serverFailure("9999", "系统错误");

    private String code;
    private String msg;

    ErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
