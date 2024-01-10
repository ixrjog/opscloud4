package com.baiyi.opscloud.zabbix.v5.feign;

import com.baiyi.opscloud.zabbix.v5.entity.ZabbixProxy;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import feign.Headers;
import feign.RequestLine;

import static com.baiyi.opscloud.zabbix.v5.feign.constant.ZabbixConstant.CONTENT_TYPE;
import static com.baiyi.opscloud.zabbix.v5.feign.constant.ZabbixConstant.REQUEST_API;

/**
 * @Author baiyi
 * @Date 2021/12/27 2:28 PM
 * @Version 1.0
 */
public interface ZabbixProxyFeign {

    @RequestLine(REQUEST_API)
    @Headers({CONTENT_TYPE})
    ZabbixProxy.QueryProxyResponse query(ZabbixRequest.DefaultRequest request);

    @RequestLine(REQUEST_API)
    @Headers({CONTENT_TYPE})
    ZabbixProxy.UpdateProxyResponse update(ZabbixRequest.DefaultRequest request);

}