package com.baiyi.opscloud.domain.generator.jumpserver;

import javax.persistence.*;
import java.util.Date;

@Table(name = "perms_assetpermission")
public class PermsAssetpermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String name;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "date_expired")
    private Date dateExpired;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_start")
    private Date dateStart;

    @Column(name = "org_id")
    private String orgId;

    private String comment;

    private Integer  actions;

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
     * @return is_active
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * @param isActive
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @return date_expired
     */
    public Date getDateExpired() {
        return dateExpired;
    }

    /**
     * @param dateExpired
     */
    public void setDateExpired(Date dateExpired) {
        this.dateExpired = dateExpired;
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
     * @return date_start
     */
    public Date getDateStart() {
        return dateStart;
    }

    /**
     * @param dateStart
     */
    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
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

    public Integer getActions() {
        return actions;
    }

    public void setActions(Integer actions) {
        this.actions = actions;
    }
}