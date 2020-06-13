package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_keybox")
public class OcKeybox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 系统账户
     */
    @Column(name = "system_user")
    private String systemUser;

    /**
     * 标题
     */
    private String title;

    /**
     * 密码
     */
    private String passphrase;

    /**
     * key类型
     */
    @Column(name = "key_type")
    private Integer keyType;

    /**
     * 默认key
     */
    @Column(name = "default_key")
    private Integer defaultKey;

    /**
     * 指纹
     */
    private String fingerprint;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    /**
     * 公钥
     */
    @Column(name = "public_key")
    private String publicKey;

    /**
     * 私钥
     */
    @Column(name = "private_key")
    private String privateKey;

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
     * 获取系统账户
     *
     * @return system_user - 系统账户
     */
    public String getSystemUser() {
        return systemUser;
    }

    /**
     * 设置系统账户
     *
     * @param systemUser 系统账户
     */
    public void setSystemUser(String systemUser) {
        this.systemUser = systemUser;
    }

    /**
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取密码
     *
     * @return passphrase - 密码
     */
    public String getPassphrase() {
        return passphrase;
    }

    /**
     * 设置密码
     *
     * @param passphrase 密码
     */
    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    /**
     * 获取key类型
     *
     * @return key_type - key类型
     */
    public Integer getKeyType() {
        return keyType;
    }

    /**
     * 设置key类型
     *
     * @param keyType key类型
     */
    public void setKeyType(Integer keyType) {
        this.keyType = keyType;
    }

    /**
     * 获取默认key
     *
     * @return default_key - 默认key
     */
    public Integer getDefaultKey() {
        return defaultKey;
    }

    /**
     * 设置默认key
     *
     * @param defaultKey 默认key
     */
    public void setDefaultKey(Integer defaultKey) {
        this.defaultKey = defaultKey;
    }

    /**
     * 获取指纹
     *
     * @return fingerprint - 指纹
     */
    public String getFingerprint() {
        return fingerprint;
    }

    /**
     * 设置指纹
     *
     * @param fingerprint 指纹
     */
    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
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
     * 获取公钥
     *
     * @return public_key - 公钥
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * 设置公钥
     *
     * @param publicKey 公钥
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * 获取私钥
     *
     * @return private_key - 私钥
     */
    public String getPrivateKey() {
        return privateKey;
    }

    /**
     * 设置私钥
     *
     * @param privateKey 私钥
     */
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}