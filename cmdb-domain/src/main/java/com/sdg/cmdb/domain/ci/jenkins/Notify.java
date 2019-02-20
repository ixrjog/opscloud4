package com.sdg.cmdb.domain.ci.jenkins;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public class Notify implements Serializable {

    private static final long serialVersionUID = -6110290487255348502L;
    private String name;
    private String url;
    private Build build;

    public Notify() {

    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Build getBuild() {
        return build;
    }

    public void setBuild(Build build) {
        this.build = build;
    }


}
