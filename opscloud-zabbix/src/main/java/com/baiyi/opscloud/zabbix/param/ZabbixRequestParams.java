package com.baiyi.opscloud.zabbix.param;

import com.baiyi.opscloud.common.util.JSONUtils;
import com.baiyi.opscloud.zabbix.http.ZabbixBaseRequest;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/1/8 5:44 下午
 * @Version 1.0
 */

@Data
@Builder
public class ZabbixRequestParams implements ZabbixBaseRequest {

    private String[] params;

    @Builder.Default
    private String jsonrpc = "2.0";

    private String method;

    private String auth;
    @Builder.Default
    private Integer id = 1;

    public String getAuth(){
        return this.auth;
    }

    public void setAuth(String auth){
        this.auth = auth;
    }

    public String getMethod() {
        return this.method;
    }

    @Override
    public String toString() {
        return JSONUtils.writeValueAsString(this);
    }
}
