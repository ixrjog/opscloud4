package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_user_group")
public class OcUserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 组名
     */
    private String name;

    /**
     * 组类型
     */
    @Column(name = "grp_type")
    private Integer grpType;

    /**
     * 数据源
     */
    private String source;

    /**
     * 允许工单申请
     */
    @Column(name = "in_workorder")
    private Integer inWorkorder;

    /**
     * 创建时间
     */
    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 描述
     */
    private String comment;

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
     * 获取组名
     *
     * @return name - 组名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置组名
     *
     * @param name 组名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取组类型
     *
     * @return grp_type - 组类型
     */
    public Integer getGrpType() {
        return grpType;
    }

    /**
     * 设置组类型
     *
     * @param grpType 组类型
     */
    public void setGrpType(Integer grpType) {
        this.grpType = grpType;
    }

    /**
     * 获取数据源
     *
     * @return source - 数据源
     */
    public String getSource() {
        return source;
    }

    /**
     * 设置数据源
     *
     * @param source 数据源
     */
    public void setSource(String source) {
        this.source = source;
    }

    public Integer getInWorkorder() {
        return inWorkorder;
    }

    public void setInWorkorder(Integer inWorkorder) {
        this.inWorkorder = inWorkorder;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
}