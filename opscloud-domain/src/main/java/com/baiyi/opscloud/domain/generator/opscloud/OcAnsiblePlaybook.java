package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_ansible_playbook")
public class OcAnsiblePlaybook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 脚本uuid
     */
    @Column(name = "playbook_uuid")
    private String playbookUuid;

    /**
     * 名称
     */
    private String name;

    private String comment;

    /**
     * 创建者
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 使用类型
     */
    @Column(name = "use_type")
    private Integer useType;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 脚本内容
     */
    private String playbook;

    /**
     * task_tags配置项目
     */
    private String tags;

    /**
     * 外部变量配置
     */
    @Column(name = "extra_vars")
    private String extraVars;

    @Column(name = "user_detail")
    private String userDetail;

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
     * 获取脚本uuid
     *
     * @return playbook_uuid - 脚本uuid
     */
    public String getPlaybookUuid() {
        return playbookUuid;
    }

    /**
     * 设置脚本uuid
     *
     * @param playbookUuid 脚本uuid
     */
    public void setPlaybookUuid(String playbookUuid) {
        this.playbookUuid = playbookUuid;
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
     * 获取创建者
     *
     * @return user_id - 创建者
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置创建者
     *
     * @param userId 创建者
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取使用类型
     *
     * @return use_type - 使用类型
     */
    public Integer getUseType() {
        return useType;
    }

    /**
     * 设置使用类型
     *
     * @param useType 使用类型
     */
    public void setUseType(Integer useType) {
        this.useType = useType;
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
     * 获取脚本内容
     *
     * @return playbook - 脚本内容
     */
    public String getPlaybook() {
        return playbook;
    }

    /**
     * 设置脚本内容
     *
     * @param playbook 脚本内容
     */
    public void setPlaybook(String playbook) {
        this.playbook = playbook;
    }

    /**
     * 获取task_tags配置项目
     *
     * @return tags - task_tags配置项目
     */
    public String getTags() {
        return tags;
    }

    /**
     * 设置task_tags配置项目
     *
     * @param tags task_tags配置项目
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * 获取外部变量配置
     *
     * @return extra_vars - 外部变量配置
     */
    public String getExtraVars() {
        return extraVars;
    }

    /**
     * 设置外部变量配置
     *
     * @param extraVars 外部变量配置
     */
    public void setExtraVars(String extraVars) {
        this.extraVars = extraVars;
    }

    /**
     * @return user_detail
     */
    public String getUserDetail() {
        return userDetail;
    }

    /**
     * @param userDetail
     */
    public void setUserDetail(String userDetail) {
        this.userDetail = userDetail;
    }
}