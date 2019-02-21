package com.sdg.cmdb.domain.zabbix;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sdg.cmdb.domain.zabbix.response.ZabbixResponseTemplate;
import lombok.Data;

import java.io.Serializable;

@Data
public class ZabbixTemplateDO implements Serializable {
    private static final long serialVersionUID = 2360429032287281955L;

    private long id;

    private String templateName;

    private String templateid;

    private boolean choose = false;

    private int enabled = 0;

    private String gmtCreate;

    private String gmtModify;


    @Override
    public String toString() {
       return JSON.toJSONString(this);
    }

    public ZabbixTemplateDO() { }

    public ZabbixTemplateDO(String templateName) {
        this.templateName = templateName;
    }

    public ZabbixTemplateDO(String templateName, int enabled) {
        this.templateName = templateName;
        this.enabled = enabled;
    }

    public ZabbixTemplateDO(String templateName, String templateid, int enabled) {
        this.templateName = templateName;
        this.templateid = templateid;
        this.enabled = enabled;
    }

    public ZabbixTemplateDO(JSONObject template, int enabled) {
        try {
            this.templateName = template.getString("name");
            this.templateid = template.getString("templateid");
            this.enabled = enabled;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ZabbixTemplateDO(ZabbixResponseTemplate template, int enabled) {
        try {
            this.templateName = template.getName();
            this.templateid = template.getTemplateid();
            this.enabled = enabled;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
