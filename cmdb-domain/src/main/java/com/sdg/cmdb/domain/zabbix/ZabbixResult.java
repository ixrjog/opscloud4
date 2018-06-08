package com.sdg.cmdb.domain.zabbix;

import java.io.Serializable;

public class ZabbixResult implements Serializable {
    private static final long serialVersionUID = -3157268706890618170L;

    private String itemid;

    private String ns;

    private String clock;

    private String value;

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getNs() {
        return ns;
    }

    public void setNs(String ns) {
        this.ns = ns;
    }

    public String getClock() {
        return clock;
    }

    public void setClock(String clock) {
        this.clock = clock;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
