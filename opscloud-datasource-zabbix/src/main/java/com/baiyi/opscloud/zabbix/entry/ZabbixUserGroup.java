package com.baiyi.opscloud.zabbix.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/28 1:23 下午
 * @Since 1.0
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZabbixUserGroup {

    @JsonProperty("usrgrpid")
    private String usrgrpid;

    private String name;

    /**
     * 组中用户的前端身份验证方法。
     * 0 - (default) 使用系统默认身份验证方法;
     * 1 - 使用内部认证;
     * 2 - 禁止访问前端。
     */
    @JsonProperty("gui_access")
    private String guiAccess;


    /**
     * 用户组是启用还是禁用。
     * 0 - (default) 启用;
     * 1 - 禁用。
     */
    @JsonProperty("users_status")
    private String status;

    private List<ZabbixUser> users;
}
