package com.sdg.cmdb.service.jms.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class SystemUser {
    private String id;

    private String name;

    private String username;

    private String password;

    private String private_key;

    private String public_key;

    private String comment;

    private Date date_created;

    private Date date_updated;

    private String created_by;

    private Integer priority;

    private String protocol;

    private Boolean auto_push;

    private String sudo;

    private String shell;

    private String[] nodes;

    private String login_mode;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public Date getDate_updated() {
        return date_updated;
    }

    public void setDate_updated(Date date_updated) {
        this.date_updated = date_updated;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Boolean getAuto_push() {
        return auto_push;
    }

    public void setAuto_push(Boolean auto_push) {
        this.auto_push = auto_push;
    }

    public String getSudo() {
        return sudo;
    }

    public void setSudo(String sudo) {
        this.sudo = sudo;
    }

    public String getShell() {
        return shell;
    }

    public void setShell(String shell) {
        this.shell = shell;
    }

    public String[] getNodes() {
        return nodes;
    }

    public void setNodes(String[] nodes) {
        this.nodes = nodes;
    }

    public String getLogin_mode() {
        return login_mode;
    }

    public void setLogin_mode(String login_mode) {
        this.login_mode = login_mode;
    }

}