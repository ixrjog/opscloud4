package com.sdg.cmdb.domain.api;

import java.io.Serializable;

public class ApiTokeDO implements Serializable {

    private static final long serialVersionUID = 335900974292027066L;

    private long id;

    private String authToke;

    private String name;

    private String content;

    private int authType;

    private String gmtModify;

    private String gmtCreate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthToke() {
        return authToke;
    }

    public void setAuthToke(String authToke) {
        this.authToke = authToke;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAuthType() {
        return authType;
    }

    public void setAuthType(int authType) {
        this.authType = authType;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
