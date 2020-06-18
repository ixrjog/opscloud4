package com.baiyi.opscloud.zabbix.entry;


import lombok.Data;

@Data
public class ZabbixMacro {

    // {"result":[{"macros":[{"macro":"{$TEST_KEY}","value":"Abc"}],"hostid":"10456"}],"id":2,"jsonrpc":"2.0"}

    private String macro;
    private String value;


}
