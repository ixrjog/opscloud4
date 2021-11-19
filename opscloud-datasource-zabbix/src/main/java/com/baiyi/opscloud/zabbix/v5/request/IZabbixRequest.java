package com.baiyi.opscloud.zabbix.v5.request;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/28 1:18 下午
 * @Since 1.0
 */

public interface IZabbixRequest {

    void setMethod(String method);

    void setAuth(String auth);

    String getMethod();

    String getAuth();

}
