package com.baiyi.opscloud.zabbix.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/1 1:52 下午
 * @Since 1.0
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZabbixHostGroup implements Serializable {

    @JsonProperty("groupid")
    private String groupid;

    private String name;

    /**
     * 主机组的来源。
     * 0 - 普通的主机组;
     * 4 - 被发现的主机组。
     */
    private Integer flags;

    /**
     * 无论该组是否由系统内部使用，内部组无法被删除。
     * 0 - (默认) 不是内部；
     * 1 - 内部。
     */
    private Integer internal;
}
