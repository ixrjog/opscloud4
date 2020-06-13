package com.baiyi.opscloud.zabbix.entry;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/1/7 5:21 下午
 * @Version 1.0
 */
@Builder
@Data
public class ZabbixUserMedia {

    public static final int MEDIATYPE_MAIL = 1;
    public static final int MEDIATYPE_PHONE = 3;


    /**
     * 1 : mail
     * 3 : phone
     */
    private Integer mediatypeid;
    private String sendto;
    @Builder.Default
    private Integer active = 0;
    @Builder.Default
    private Integer severity = 48;
    @Builder.Default
    private String period = "1-7,00:00-24:00";

}
