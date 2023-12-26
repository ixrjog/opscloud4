package com.baiyi.opscloud.zabbix.v5.feign;

import com.baiyi.opscloud.zabbix.v5.feign.constant.ZabbixConstant;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixLogin;
import feign.Headers;
import feign.RequestLine;

/**
 * @Author baiyi
 * @Date 2021/11/17 10:46 上午
 * @Version 1.0
 */
public interface ZabbixLoginFeign extends ZabbixConstant {

    @RequestLine(REQUEST_API)
    @Headers({CONTENT_TYPE})
    ZabbixLogin.LoginAuth userLogin(ZabbixRequest.DefaultRequest request);

}