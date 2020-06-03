package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_tag")
public class OcTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 标签Key,全局唯一
     */
    @Column(name = "tag_key")
    private String tagKey;

    /**
     * 颜色值
     */
    private String color;

    /**
     * 描述
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
     * 获取标签Key,全局唯一
     *
     * @return tag_key - 标签Key,全局唯一
     */
    public String getTagKey() {
        return tagKey;
    }

    /**
     * 设置标签Key,全局唯一
     *
     * @param tagKey 标签Key,全局唯一
     */
    public void setTagKey(String tagKey) {
        this.tagKey = tagKey;
    }

    /**
     * 获取颜色值
     *
     * @return color - 颜色值
     */
    public String getColor() {
        return color;
    }

    /**
     * 设置颜色值
     *
     * @param color 颜色值
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * 获取描述
     *
     * @return comment - 描述
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置描述
     *
     * @param comment 描述
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