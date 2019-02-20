package com.sdg.cmdb.domain.auth;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/8/9.
 */
public class LdapGroup implements Serializable {
    private static final long serialVersionUID = 1666739342255019488L;

    private String name;

    private String content;

    // 是否绑定
    private boolean bind=false;

    @Override
    public String toString() {
        return "LdapGroup{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", bind='" + bind + '\'' +
                '}';
    }

    public LdapGroup() {

    }

    public LdapGroup(String name, Boolean bind ) {
        this.name = name;
        this.bind = bind;
    }

    public LdapGroup(String name, Boolean bind ,String content) {
        this.name = name;
        this.bind = bind;
        this.content =content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBind() {
        return bind;
    }

    public void setBind(boolean bind) {
        this.bind = bind;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
