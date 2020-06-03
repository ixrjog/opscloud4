package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_user_document")
public class OcUserDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    private String username;

    @Column(name = "doc_title")
    private String docTitle;

    /**
     * 文档类型
     */
    @Column(name = "doc_type")
    private Integer docType;

    private String comment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 文档内容
     */
    @Column(name = "doc_content")
    private String docContent;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
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
     * @return doc_title
     */
    public String getDocTitle() {
        return docTitle;
    }

    /**
     * @param docTitle
     */
    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    /**
     * 获取文档类型
     *
     * @return doc_type - 文档类型
     */
    public Integer getDocType() {
        return docType;
    }

    /**
     * 设置文档类型
     *
     * @param docType 文档类型
     */
    public void setDocType(Integer docType) {
        this.docType = docType;
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
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取文档内容
     *
     * @return doc_content - 文档内容
     */
    public String getDocContent() {
        return docContent;
    }

    /**
     * 设置文档内容
     *
     * @param docContent 文档内容
     */
    public void setDocContent(String docContent) {
        this.docContent = docContent;
    }
}