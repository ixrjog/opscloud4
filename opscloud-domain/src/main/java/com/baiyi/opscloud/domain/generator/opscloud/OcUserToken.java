package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_user_token")
public class OcUserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户登录名
     */
    private String username;

    /**
     * 登录唯一标识
     */
    private String token;

    /**
     * 是否无效。0：无效；1：有效
     */
    private Boolean valid;

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
     * 获取用户登录名
     *
     * @return username - 用户登录名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户登录名
     *
     * @param username 用户登录名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取登录唯一标识
     *
     * @return token - 登录唯一标识
     */
    public String getToken() {
        return token;
    }

    /**
     * 设置登录唯一标识
     *
     * @param token 登录唯一标识
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 获取是否无效。0：无效；1：有效
     *
     * @return valid - 是否无效。0：无效；1：有效
     */
    public Boolean getValid() {
        return valid;
    }

    /**
     * 设置是否无效。0：无效；1：有效
     *
     * @param valid 是否无效。0：无效；1：有效
     */
    public void setValid(Boolean valid) {
        this.valid = valid;
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