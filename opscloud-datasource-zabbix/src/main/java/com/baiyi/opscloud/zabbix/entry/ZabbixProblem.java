package com.baiyi.opscloud.zabbix.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/2 6:59 下午
 * @Since 1.0
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZabbixProblem {


    //@JsonProperty("eventid")
    private String eventid;

    private String name;

    private Integer object;

    //@JsonProperty("objectid")
    private String objectid;

    // 问题事件创建的时间
    private Date clock;

    /**
     * 问题当前级别
     * 0-未定义
     * 1-信息
     * 2-警告
     * 3-一般严重
     * 4-严重
     * 5-灾难
     */
    private Integer severity;
}
