package com.baiyi.opscloud.zabbix.entity;

import com.baiyi.opscloud.zabbix.mapper.ZabbixMapper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/28 1:44 下午
 * @Since 1.0
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZabbixMedia implements Serializable {

    public interface MediaType {
        int MAIL = 1;
        int PHONE = 3;
    }


    /**
     * 媒介类型的传输方式
     * 1-email
     * 3-phone
     */
    // @JsonProperty("mediatypeid")
    private String mediatypeid;

    /**
     * 地址, 用户名或者接收方的其他标识符。
     * 如果媒介类型是电子邮件, 值被设置为 数组。 其他类型值被设置为字符串。
     */
    //@JsonProperty("sendto")
    private JsonNode sendto;


    @JsonProperty
    public Object getSendto() {
        if ("1".equals(mediatypeid)) {
            return ZabbixMapper.mapperList(this.sendto, String.class);
        }
        return ZabbixMapper.mapper(this.sendto, String.class);
    }

    /**
     * 是否启用媒体。
     * 0 - (默认) enabled;
     * 1 - disabled.
     */
    @Builder.Default
    private Integer active = 0;

    /**
     * 触发发送通知告警级别
     */
    @Builder.Default
    private Integer severity = 48;

    /**
     * 当通知可以作为 时间段 发送或者用分号隔开用户宏
     */
    @Builder.Default
    private String period = "1-7,00:00-24:00";
}
