package com.baiyi.opscloud.zabbix.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/12/9 3:15 下午
 * @Version 1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ZabbixMedia {

    /**
     *   "medias":[
     *             {
     *                 "mediaid":"1",
     *                 "userid":"3",
     *                 "mediatypeid":"1",
     *                 "sendto":[
     *                     "baiyi@xinc818.group"
     *                 ],
     *                 "active":"0",
     *                 "severity":"48",
     *                 "period":"1-7,00:00-24:00"
     *             },
     *             {
     *                 "mediaid":"2",
     *                 "userid":"3",
     *                 "mediatypeid":"3",
     *                 "sendto":"13456768044",
     *                 "active":"0",
     *                 "severity":"48",
     *                 "period":"1-7,00:00-24:00"
     *             }
     *         ]
     */

    private String mediaid;
    private String userid;
    private String mediatypeid;
    private JsonNode sendto;
    private String active;
    private String severity;
    private String period;

}
