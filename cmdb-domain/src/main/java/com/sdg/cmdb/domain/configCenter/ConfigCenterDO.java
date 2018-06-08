package com.sdg.cmdb.domain.configCenter;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/5/26.
 */
public class ConfigCenterDO implements Serializable {

    private static final long serialVersionUID = -637891957421927825L;
    private long id;

    private String itemGroup;

    private String env;

    private String item;


    private String value;


    private String content;


    private String gmtCreate;

    private String gmtModify;

    @Override
    public String toString() {
        return "ConfigCenterDO{" +
                "id=" + id +
                ", itemGroup='" + itemGroup + '\'' +
                ", env='" + env + '\'' +
                ", item='" + item + '\'' +
                ", value=" + value +
                ", content='" + content + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
