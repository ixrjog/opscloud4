package com.baiyi.opscloud.zabbix.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/25 3:34 下午
 * @Since 1.0
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZabbixUser {

   // @JsonProperty("userid")
    private String userid;
    private String alias;
    private String name;

    /**
     * 姓
     */
    private String surname;

    /**
     * 允许自动登录。
     * 0 - (default) 禁止自动登录;
     * 1 -允许自动登录。
     */
    //@JsonProperty("autologin")
    private Integer autologin;

    /**
     * 会话过期时间。 接受具有后缀的秒或时间单位。 如果设置为 0s, 用户登录会话永远不会过期。
     */
    //@JsonProperty("autologout")
    private String autologout;

    /**
     * 用户类型。
     * 1 - (default) Zabbix user;
     * 2 - Zabbix admin;
     * 3 - Zabbix super admin.
     */
    private Integer type;
    //@JsonProperty("sessionid")
    private String sessionid;

    /**
     * 用户使用的媒体
     */
    private List<ZabbixMedia> medias;
}
