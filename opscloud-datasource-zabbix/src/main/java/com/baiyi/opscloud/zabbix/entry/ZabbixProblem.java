package com.baiyi.opscloud.zabbix.entry;

import com.baiyi.opscloud.domain.base.IRecover;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/2 6:59 下午
 * @Since 1.0
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZabbixProblem implements IRecover, Serializable {

    private static final long serialVersionUID = -7945069465875128322L;
    private String eventid;

    private String name;

    private Integer object;

    private String objectid;

    // 问题事件创建的时间
    private Long clock;

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

    /**
     * 事件恢复的UNIT时间戳
     */
    @JsonProperty("r_clock")
    private Long rClock;

    @Override
    public boolean isRecover() {
        return rClock != 0;
    }
}
