package com.baiyi.opscloud.zabbix.v5.feign;

import com.baiyi.opscloud.zabbix.v5.entity.ZabbixUser;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import feign.Headers;
import feign.RequestLine;

import static com.baiyi.opscloud.zabbix.v5.feign.constant.ZabbixConstant.CONTENT_TYPE;
import static com.baiyi.opscloud.zabbix.v5.feign.constant.ZabbixConstant.REQUEST_API;

/**
 * @Author baiyi
 * @Date 2021/11/19 2:37 下午
 * @Version 1.0
 */
public interface ZabbixUserFeign {

    @RequestLine(REQUEST_API)
    @Headers({CONTENT_TYPE})
    ZabbixUser.QueryUserResponse query(ZabbixRequest.DefaultRequest request);

    @RequestLine(REQUEST_API)
    @Headers({CONTENT_TYPE})
    ZabbixUser.UpdateUserResponse update(ZabbixRequest.DefaultRequest request);

    @RequestLine(REQUEST_API)
    @Headers({CONTENT_TYPE})
    ZabbixUser.CreateUserResponse create(ZabbixRequest.DefaultRequest request);

    @RequestLine(REQUEST_API)
    @Headers({CONTENT_TYPE})
    ZabbixUser.DeleteUserResponse delete(ZabbixRequest.DeleteRequest request);

}