package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_aliyun_slb_sc")
public class OcAliyunSlbSc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 服务器证书ID
     */
    @Column(name = "server_certificate_id")
    private String serverCertificateId;

    /**
     * 服务器证书名称
     */
    @Column(name = "server_certificate_name")
    private String serverCertificateName;

    /**
     * 服务器证书的地域
     */
    @Column(name = "region_id")
    private String regionId;

    private String fingerprint;

    /**
     * 阿里云证书服务的服务器证书id
     */
    @Column(name = "ali_cloud_certificate_id")
    private String aliCloudCertificateId;

    /**
     * 阿里云证书服务的服务器证书名称
     */
    @Column(name = "ali_cloud_certificate_name")
    private String aliCloudCertificateName;

    /**
     * 域名，对应证书的CommonName字段
     */
    @Column(name = "common_name")
    private String commonName;

    /**
     * 过期时间
     */
    @Column(name = "expire_time")
    private String expireTime;

    /**
     * 过期时间戳
     */
    @Column(name = "expire_time_stamp")
    private Long expireTimeStamp;

    /**
     * 更换的服务器证书ID
     */
    @Column(name = "update_server_certificate_id")
    private String updateServerCertificateId;

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
     * 获取服务器证书ID
     *
     * @return server_certificate_id - 服务器证书ID
     */
    public String getServerCertificateId() {
        return serverCertificateId;
    }

    /**
     * 设置服务器证书ID
     *
     * @param serverCertificateId 服务器证书ID
     */
    public void setServerCertificateId(String serverCertificateId) {
        this.serverCertificateId = serverCertificateId;
    }

    /**
     * 获取服务器证书名称
     *
     * @return server_certificate_name - 服务器证书名称
     */
    public String getServerCertificateName() {
        return serverCertificateName;
    }

    /**
     * 设置服务器证书名称
     *
     * @param serverCertificateName 服务器证书名称
     */
    public void setServerCertificateName(String serverCertificateName) {
        this.serverCertificateName = serverCertificateName;
    }

    /**
     * 获取服务器证书的地域
     *
     * @return region_id - 服务器证书的地域
     */
    public String getRegionId() {
        return regionId;
    }

    /**
     * 设置服务器证书的地域
     *
     * @param regionId 服务器证书的地域
     */
    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    /**
     * @return fingerprint
     */
    public String getFingerprint() {
        return fingerprint;
    }

    /**
     * @param fingerprint
     */
    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    /**
     * 获取阿里云证书服务的服务器证书id
     *
     * @return ali_cloud_certificate_id - 阿里云证书服务的服务器证书id
     */
    public String getAliCloudCertificateId() {
        return aliCloudCertificateId;
    }

    /**
     * 设置阿里云证书服务的服务器证书id
     *
     * @param aliCloudCertificateId 阿里云证书服务的服务器证书id
     */
    public void setAliCloudCertificateId(String aliCloudCertificateId) {
        this.aliCloudCertificateId = aliCloudCertificateId;
    }

    /**
     * 获取阿里云证书服务的服务器证书名称
     *
     * @return ali_cloud_certificate_name - 阿里云证书服务的服务器证书名称
     */
    public String getAliCloudCertificateName() {
        return aliCloudCertificateName;
    }

    /**
     * 设置阿里云证书服务的服务器证书名称
     *
     * @param aliCloudCertificateName 阿里云证书服务的服务器证书名称
     */
    public void setAliCloudCertificateName(String aliCloudCertificateName) {
        this.aliCloudCertificateName = aliCloudCertificateName;
    }

    /**
     * 获取域名，对应证书的CommonName字段
     *
     * @return common_name - 域名，对应证书的CommonName字段
     */
    public String getCommonName() {
        return commonName;
    }

    /**
     * 设置域名，对应证书的CommonName字段
     *
     * @param commonName 域名，对应证书的CommonName字段
     */
    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    /**
     * 获取过期时间
     *
     * @return expire_time - 过期时间
     */
    public String getExpireTime() {
        return expireTime;
    }

    /**
     * 设置过期时间
     *
     * @param expireTime 过期时间
     */
    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    /**
     * 获取过期时间戳
     *
     * @return expire_time_stamp - 过期时间戳
     */
    public Long getExpireTimeStamp() {
        return expireTimeStamp;
    }

    /**
     * 设置过期时间戳
     *
     * @param expireTimeStamp 过期时间戳
     */
    public void setExpireTimeStamp(Long expireTimeStamp) {
        this.expireTimeStamp = expireTimeStamp;
    }

    /**
     * 获取更换的服务器证书ID
     *
     * @return update_server_certificate_id - 更换的服务器证书ID
     */
    public String getUpdateServerCertificateId() {
        return updateServerCertificateId;
    }

    /**
     * 设置更换的服务器证书ID
     *
     * @param updateServerCertificateId 更换的服务器证书ID
     */
    public void setUpdateServerCertificateId(String updateServerCertificateId) {
        this.updateServerCertificateId = updateServerCertificateId;
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