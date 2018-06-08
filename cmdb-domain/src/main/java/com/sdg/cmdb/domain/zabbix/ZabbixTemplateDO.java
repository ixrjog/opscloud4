package com.sdg.cmdb.domain.zabbix;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

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
        return "ZabbixTemplateDO{" +
                "id=" + id +
                ", templateName='" + templateName + '\'' +
                ", templateid='" + templateid + '\'' +
                ", enabled='" + enabled + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                '}';
    }

    public ZabbixTemplateDO() {

    }


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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateid() {
        return templateid;
    }

    public void setTemplateid(String templateid) {
        this.templateid = templateid;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }
}
