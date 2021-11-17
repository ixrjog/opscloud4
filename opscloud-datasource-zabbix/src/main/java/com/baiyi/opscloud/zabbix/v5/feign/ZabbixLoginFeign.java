package com.baiyi.opscloud.zabbix.v5.feign;

import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import com.baiyi.opscloud.zabbix.v5.response.LoginResponse;
import feign.Headers;
import feign.RequestLine;

/**
 * @Author baiyi
 * @Date 2021/11/17 10:46 上午
 * @Version 1.0
 */
public interface ZabbixLoginFeign {

    @RequestLine("POST api_jsonrpc.php")
    @Headers({"Content-Type: application/json-rpc"})
    LoginResponse.LoginAuth userLogin(ZabbixRequest.DefaultRequest request);

}
