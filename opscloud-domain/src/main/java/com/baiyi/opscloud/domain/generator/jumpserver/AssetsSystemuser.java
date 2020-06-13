package com.baiyi.opscloud.domain.generator.jumpserver;

import javax.persistence.*;
import java.util.Date;

@Table(name = "assets_systemuser")
public class AssetsSystemuser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String name;

    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_updated")
    private Date dateUpdated;

    @Column(name = "created_by")
    private String createdBy;

    private Integer priority;

    private String protocol;

    @Column(name = "auto_push")
    private Boolean autoPush;

    private String shell;

    @Column(name = "login_mode")
    private String loginMode;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "private_key")
    private String privateKey;

    @Column(name = "public_key")
    private String publicKey;

    private String comment;

    private String sudo;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return _password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return date_created
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * @param dateCreated
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * @return date_updated
     */
    public Date getDateUpdated() {
        return dateUpdated;
    }

    /**
     * @param dateUpdated
     */
    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    /**
     * @return created_by
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return priority
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * @param priority
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * @return protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * @param protocol
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * @return auto_push
     */
    public Boolean getAutoPush() {
        return autoPush;
    }

    /**
     * @param autoPush
     */
    public void setAutoPush(Boolean autoPush) {
        this.autoPush = autoPush;
    }

    /**
     * @return shell
     */
    public String getShell() {
        return shell;
    }

    /**
     * @param shell
     */
    public void setShell(String shell) {
        this.shell = shell;
    }

    /**
     * @return login_mode
     */
    public String getLoginMode() {
        return loginMode;
    }

    /**
     * @param loginMode
     */
    public void setLoginMode(String loginMode) {
        this.loginMode = loginMode;
    }

    /**
     * @return org_id
     */
    public String getOrgId() {
        return orgId;
    }

    /**
     * @param orgId
     */
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /**
     * @return _private_key
     */
    public String getPrivateKey() {
        return privateKey;
    }

    /**
     * @param privateKey
     */
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    /**
     * @return _public_key
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * @param publicKey
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * @return comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return sudo
     */
    public String getSudo() {
        return sudo;
    }

    /**
     * @param sudo
     */
    public void setSudo(String sudo) {
        this.sudo = sudo;
    }
}