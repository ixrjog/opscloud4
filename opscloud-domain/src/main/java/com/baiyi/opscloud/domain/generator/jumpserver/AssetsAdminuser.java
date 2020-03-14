package com.baiyi.opscloud.domain.generator.jumpserver;

import javax.persistence.*;
import java.util.Date;

@Table(name = "assets_adminuser")
public class AssetsAdminuser {
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

    private Boolean become;

    @Column(name = "become_method")
    private String becomeMethod;

    @Column(name = "become_user")
    private String becomeUser;

    @Column(name = "_become_pass")
    private String becomePass;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "private_key")
    private String privateKey;

    @Column(name = "public_key")
    private String publicKey;

    private String comment;

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
     * @return become
     */
    public Boolean getBecome() {
        return become;
    }

    /**
     * @param become
     */
    public void setBecome(Boolean become) {
        this.become = become;
    }

    /**
     * @return become_method
     */
    public String getBecomeMethod() {
        return becomeMethod;
    }

    /**
     * @param becomeMethod
     */
    public void setBecomeMethod(String becomeMethod) {
        this.becomeMethod = becomeMethod;
    }

    /**
     * @return become_user
     */
    public String getBecomeUser() {
        return becomeUser;
    }

    /**
     * @param becomeUser
     */
    public void setBecomeUser(String becomeUser) {
        this.becomeUser = becomeUser;
    }

    /**
     * @return _become_pass
     */
    public String getBecomePass() {
        return becomePass;
    }

    /**
     * @param becomePass
     */
    public void setBecomePass(String becomePass) {
        this.becomePass = becomePass;
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
}