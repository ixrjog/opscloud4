package com.baiyi.opscloud.zabbix.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/2 3:33 下午
 * @Since 1.0
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZabbixTrigger {
    @JsonProperty("triggerid")
    private String triggerId;
    private String description;
    /**
     * 触发器的严重性级别。
     * 0 - (默认) 未分类；
     * 1 - 信息；
     * 2 - 警告；
     * 3 - 一般严重；
     * 4 - 严重；
     * 5 - 灾难。
     */
    private String priority;
    //触发器最后更改其状态的时间
    @JsonProperty("lastchange")
    private Date lastChange;
    /**
     * 触发器是否处于启用状态或禁用状态。
     * 0 - (默认) 启用；
     * 1 - 禁用。
     */
    private Integer value;
    private List<ZabbixHost> hosts;

}
