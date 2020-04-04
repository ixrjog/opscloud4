package com.baiyi.opscloud.zabbix.builder;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/4/3 2:53 下午
 * @Version 1.0
 */
@Data
@Builder
public class ZabbixOpmessageBO {

    @Builder.Default
    private Integer mediatypeid = 0;
    @Builder.Default
    private Integer default_msg = 1;
    @Builder.Default
    private String subject = "{TRIGGER.STATUS}: {TRIGGER.NAME}";
    @Builder.Default
    private String message = "Trigger: {TRIGGER.NAME}\r\nTrigger status: {TRIGGER.STATUS}\r\nTrigger severity: {TRIGGER.SEVERITY}\r\nTrigger URL: {TRIGGER.URL}\r\n\r\nItem values:\r\n\r\n1. {ITEM.NAME1} ({HOST.NAME1}:{ITEM.KEY1}): {ITEM.VALUE1}\r\n2. {ITEM.NAME2} ({HOST.NAME2}:{ITEM.KEY2}): {ITEM.VALUE2}\r\n3. {ITEM.NAME3} ({HOST.NAME3}:{ITEM.KEY3}): {ITEM.VALUE3}\r\n\r\nOriginal event ID: {EVENT.ID}";

}
