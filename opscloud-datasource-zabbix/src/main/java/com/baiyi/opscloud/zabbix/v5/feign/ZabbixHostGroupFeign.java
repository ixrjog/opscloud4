package com.baiyi.opscloud.zabbix.v5.feign;

import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import feign.Headers;
import feign.RequestLine;

import static com.baiyi.opscloud.zabbix.v5.feign.constant.ZabbixConstant.CONTENT_TYPE;
import static com.baiyi.opscloud.zabbix.v5.feign.constant.ZabbixConstant.REQUEST_API;

/**
 * @Author baiyi
 * @Date 2021/11/19 2:04 下午
 * @Version 1.0
 */
public interface ZabbixHostGroupFeign {

    @RequestLine(REQUEST_API)
    @Headers({CONTENT_TYPE})
    ZabbixHostGroup.QueryHostGroupResponse query(ZabbixRequest.DefaultRequest request);

    @RequestLine(REQUEST_API)
    @Headers({CONTENT_TYPE})
    ZabbixHostGroup.CreateHostGroupResponse create(ZabbixRequest.DefaultRequest request);

}