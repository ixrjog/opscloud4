package com.baiyi.opscloud.zabbix.v5.feign;

import com.baiyi.opscloud.zabbix.v5.entity.ZabbixTrigger;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import feign.Headers;
import feign.RequestLine;

import static com.baiyi.opscloud.zabbix.v5.feign.constant.ZabbixConstant.CONTENT_TYPE;
import static com.baiyi.opscloud.zabbix.v5.feign.constant.ZabbixConstant.REQUEST_API;

/**
 * @Author baiyi
 * @Date 2021/11/19 11:13 上午
 * @Version 1.0
 */
public interface ZabbixTriggerFeign {

    @RequestLine(REQUEST_API)
    @Headers({CONTENT_TYPE})
    ZabbixTrigger.QueryTriggerResponse query(ZabbixRequest.DefaultRequest request);

}