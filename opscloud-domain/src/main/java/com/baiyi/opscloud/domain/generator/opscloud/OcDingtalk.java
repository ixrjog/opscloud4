package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_dingtalk")
public class OcDingtalk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 搜索用关键字
     */
    @Column(name = "dingtalk_key")
    private String dingtalkKey;

    /**
     * token
     */
    @Column(name = "dingtalk_token")
    private String dingtalkToken;

    /**
     * 说明
     */
    private String comment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

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
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取搜索用关键字
     *
     * @return dingtalk_key - 搜索用关键字
     */
    public String getDingtalkKey() {
        return dingtalkKey;
    }

    /**
     * 设置搜索用关键字
     *
     * @param dingtalkKey 搜索用关键字
     */
    public void setDingtalkKey(String dingtalkKey) {
        this.dingtalkKey = dingtalkKey;
    }

    /**
     * 获取token
     *
     * @return dingtalk_token - token
     */
    public String getDingtalkToken() {
        return dingtalkToken;
    }

    /**
     * 设置token
     *
     * @param dingtalkToken token
     */
    public void setDingtalkToken(String dingtalkToken) {
        this.dingtalkToken = dingtalkToken;
    }

    /**
     * 获取说明
     *
     * @return comment - 说明
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置说明
     *
     * @param comment 说明
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
}