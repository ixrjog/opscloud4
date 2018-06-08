package com.sdg.cmdb.domain.zabbix;

import java.io.Serializable;

public class ZabbixTemplateVO extends ZabbixTemplateDO implements Serializable {
    private static final long serialVersionUID = 5700598289324296705L;


    private boolean choose = false;

    public ZabbixTemplateVO() {

    }

    public ZabbixTemplateVO(ZabbixTemplateDO zabbixTemplateDO) {
        setId(zabbixTemplateDO.getId());
        setTemplateName(zabbixTemplateDO.getTemplateName());
        setTemplateid(zabbixTemplateDO.getTemplateid());
        setEnabled(zabbixTemplateDO.getEnabled());
        setGmtCreate(zabbixTemplateDO.getGmtCreate());
        setGmtModify(zabbixTemplateDO.getGmtModify());
    }

    public ZabbixTemplateVO(ZabbixTemplateDO zabbixTemplateDO, boolean choose) {
        setId(zabbixTemplateDO.getId());
        setTemplateName(zabbixTemplateDO.getTemplateName());
        setTemplateid(zabbixTemplateDO.getTemplateid());
        setEnabled(zabbixTemplateDO.getEnabled());
        setGmtCreate(zabbixTemplateDO.getGmtCreate());
        setGmtModify(zabbixTemplateDO.getGmtModify());
        this.choose =choose;
    }

    @Override
    public boolean isChoose() {
        return choose;
    }

    @Override
    public void setChoose(boolean choose) {
        this.choose = choose;
    }
}
