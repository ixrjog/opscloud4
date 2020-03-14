package com.baiyi.opscloud.domain.generator.jumpserver;

import javax.persistence.*;
import java.util.Date;

@Table(name = "assets_node")
public class AssetsNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "`key`")
    private String key;

    @Column(name = "`value`")
    private String value;

    @Column(name = "child_mark")
    private Integer childMark;

    @Column(name = "date_create")
    private Date dateCreate;

    @Column(name = "org_id")
    private String orgId;

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
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return child_mark
     */
    public Integer getChildMark() {
        return childMark;
    }

    /**
     * @param childMark
     */
    public void setChildMark(Integer childMark) {
        this.childMark = childMark;
    }

    /**
     * @return date_create
     */
    public Date getDateCreate() {
        return dateCreate;
    }

    /**
     * @param dateCreate
     */
    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
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
}