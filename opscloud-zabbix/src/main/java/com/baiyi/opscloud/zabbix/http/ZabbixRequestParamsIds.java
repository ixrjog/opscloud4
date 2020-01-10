package com.baiyi.opscloud.zabbix.http;

import com.baiyi.opscloud.common.util.JSONUtils;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/1/8 5:44 下午
 * @Version 1.0
 */

@Data
public class ZabbixRequestParamsIds extends ZabbixRequest{

    private String[] params;

    @Override
    public String toString() {
        return JSONUtils.writeValueAsString(this);
    }
}
