package com.sdg.cmdb.domain.zabbix;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class ZabbixInterface implements Serializable {
    private static final long serialVersionUID = -6002761155309249030L;

    private int type;
    private int main = 1;
    private int useip = 1;
    private String ip;
    private String dns = "";
    private String port;

    public ZabbixInterface() {
    }

    public ZabbixInterface(int type, String ip, String port) {
        this.type = type;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
