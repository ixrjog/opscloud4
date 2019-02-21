package com.sdg.cmdb.domain.zabbix.response;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class ZabbixResponseMacro implements Serializable {
    private static final long serialVersionUID = -3180622112466211272L;
    // {"result":[{"macros":[{"macro":"{$TEST_KEY}","value":"Abc"}],"hostid":"10456"}],"id":2,"jsonrpc":"2.0"}

    private String macro;
    private String value;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
