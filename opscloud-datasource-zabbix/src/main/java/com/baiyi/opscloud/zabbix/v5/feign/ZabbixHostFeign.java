package com.baiyi.opscloud.zabbix.v5.feign;

import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import feign.Headers;
import feign.RequestLine;

import static com.baiyi.opscloud.zabbix.v5.feign.constant.ZabbixConstant.CONTENT_TYPE;
import static com.baiyi.opscloud.zabbix.v5.feign.constant.ZabbixConstant.REQUEST_API;

/**
 * @Author baiyi
 * @Date 2021/11/18 1:47 下午
 * @Version 1.0
 */
public interface ZabbixHostFeign {

    @RequestLine(REQUEST_API)
    @Headers({CONTENT_TYPE})
    ZabbixHost.QueryHostResponse query(ZabbixRequest.DefaultRequest request);

    @RequestLine(REQUEST_API)
    @Headers({CONTENT_TYPE})
    ZabbixHost.UpdateHostResponse update(ZabbixRequest.DefaultRequest request);

    @RequestLine(REQUEST_API)
    @Headers({CONTENT_TYPE})
    ZabbixHost.CreateHostResponse create(ZabbixRequest.DefaultRequest request);

    @RequestLine(REQUEST_API)
    @Headers({CONTENT_TYPE})
    ZabbixHost.DeleteHostResponse delete(ZabbixRequest.DeleteRequest request);

}