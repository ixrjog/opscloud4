package com.baiyi.opscloud.zabbix.http;


/**
 * Created by liangjian on 2016/12/19.
 */

public interface ZabbixBaseRequest {

    String getAuth();

    void setAuth(String auth);

    String getMethod();

}
