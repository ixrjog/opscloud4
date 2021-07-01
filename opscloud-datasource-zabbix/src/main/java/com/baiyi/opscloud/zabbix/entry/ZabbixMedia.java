package com.baiyi.opscloud.zabbix.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/28 1:44 下午
 * @Since 1.0
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZabbixMedia {


    /**
     * 媒介类型的传输方式
     * 0-电子邮件
     * 1-脚本
     * 2-SMS
     * 3-Jabber
     * 100-Ez Texting。
     */
    @JsonProperty("mediatypeid")
    private String mediaTypeId;

    /**
     * 地址, 用户名或者接收方的其他标识符。
     * 如果媒介类型是电子邮件, 值被设置为 数组。 其他类型值被设置为  字符串。
     */
    @JsonProperty("sendto")
    private JsonNode sendTo;

    /**
     * 是否启用媒体。
     * 0 - (默认) enabled;
     * 1 - disabled.
     */
    private Integer active;

    // 触发发送通知告警级别
    private Integer severity;

    // 当通知可以作为 时间段 发送或者用分号隔开用户宏。
    private String period;
}
