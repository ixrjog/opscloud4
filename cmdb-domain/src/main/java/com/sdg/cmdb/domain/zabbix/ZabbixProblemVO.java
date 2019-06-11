package com.sdg.cmdb.domain.zabbix;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.zabbix.response.ZabbixResponseHost;
import com.sdg.cmdb.domain.zabbix.response.ZabbixResponseTrigger;
import lombok.Data;

import java.io.Serializable;

@Data
public class ZabbixProblemVO implements Serializable {
    private static final long serialVersionUID = 8268070998160631871L;

    private ZabbixResponseTrigger trigger;
    private ZabbixResponseHost host;
    private String description;
    /**
     * 0 - (default) not classified;
     * 1 - information;
     * 2 - warning;
     * 3 - average;
     * 4 - high;
     * 5 - disaster.
     */
    private String priority;
    private String backgroundColor = "#777"; // 默认灰色
    private String ago;

    public ZabbixProblemVO(ZabbixResponseTrigger trigger, ZabbixResponseHost host) {
        this.trigger = trigger;
        this.host = host;
        this.priority = trigger.getPriority();
        this.description = trigger.getDescription().replaceAll("\\{HOST\\.?NAME}", "@" + host.getHost()); // 替换掉 {HOSTNAME} | {HOST.NAME}
        setColor(priority);
    }

    public ZabbixProblemVO() {
    }

    private void setColor(String priority) {
        switch (Integer.valueOf(priority)) {
            case 2:
                this.backgroundColor = "#f0ad4e";
                break;
            case 3:
                this.backgroundColor = "#ff8700";
                break;
            case 4:
                this.backgroundColor = "#ef2929";
                break;
            case 5:
                this.backgroundColor = "#b92c28";
                break;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
