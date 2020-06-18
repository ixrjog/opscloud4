package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_ansible_script")
public class OcAnsibleScript {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 脚本uuid
     */
    @Column(name = "script_uuid")
    private String scriptUuid;

    /**
     * 名称
     */
    private String name;

    /**
     * 脚本语法（前端语法高亮）
     */
    @Column(name = "script_lang")
    private String scriptLang;

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

    /**
     * 管理员锁
     */
    @Column(name = "script_lock")
    private Integer scriptLock;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 脚本内容
     */
    @Column(name = "script_content")
    private String scriptContent;

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
     * @return script_uuid - 脚本uuid
     */
    public String getScriptUuid() {
        return scriptUuid;
    }

    /**
     * 设置脚本uuid
     *
     * @param scriptUuid 脚本uuid
     */
    public void setScriptUuid(String scriptUuid) {
        this.scriptUuid = scriptUuid;
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
     * 获取脚本语法（前端语法高亮）
     *
     * @return script_lang - 脚本语法（前端语法高亮）
     */
    public String getScriptLang() {
        return scriptLang;
    }

    /**
     * 设置脚本语法（前端语法高亮）
     *
     * @param scriptLang 脚本语法（前端语法高亮）
     */
    public void setScriptLang(String scriptLang) {
        this.scriptLang = scriptLang;
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
     * 获取管理员锁
     *
     * @return script_lock - 管理员锁
     */
    public Integer getScriptLock() {
        return scriptLock;
    }

    /**
     * 设置管理员锁
     *
     * @param scriptLock 管理员锁
     */
    public void setScriptLock(Integer scriptLock) {
        this.scriptLock = scriptLock;
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
     * @return script_content - 脚本内容
     */
    public String getScriptContent() {
        return scriptContent;
    }

    /**
     * 设置脚本内容
     *
     * @param scriptContent 脚本内容
     */
    public void setScriptContent(String scriptContent) {
        this.scriptContent = scriptContent;
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