package com.sdg.cmdb.domain.nginx;

import java.io.Serializable;

public class DomainDO implements Serializable {

    private long id;

    private String domain;


    private String content;

    private String gmtCreate;

    private String gmtModify;


    public DomainDO() {

    }

    public DomainDO(String domain) {
        this.domain = domain;
    }

    @Override
    public String toString() {
        return "DomainDO{" +
                "id=" + id +
                ", domain='" + domain + '\'' +
                ", content='" + content + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ",  gmtModify='" + gmtModify + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
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
