package com.baiyi.opscloud.zabbix.v5.feign;

import com.baiyi.opscloud.zabbix.v5.entity.ZabbixAction;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import feign.Headers;
import feign.RequestLine;

import static com.baiyi.opscloud.zabbix.v5.feign.constant.ZabbixConstant.CONTENT_TYPE;
import static com.baiyi.opscloud.zabbix.v5.feign.constant.ZabbixConstant.REQUEST_API;

/**
 * @Author baiyi
 * @Date 2021/11/19 4:20 下午
 * @Version 1.0
 */
public interface ZabbixActionFeign {

    @RequestLine(REQUEST_API)
    @Headers({CONTENT_TYPE})
    ZabbixAction.QueryActionResponse query(ZabbixRequest.DefaultRequest request);

    @RequestLine(REQUEST_API)
    @Headers({CONTENT_TYPE})
    ZabbixAction.CreateActionResponse create(ZabbixRequest.DefaultRequest request);


    @RequestLine(REQUEST_API)
    @Headers({CONTENT_TYPE})
    ZabbixAction.DeleteActionResponse delete(ZabbixRequest.DeleteRequest request);

}