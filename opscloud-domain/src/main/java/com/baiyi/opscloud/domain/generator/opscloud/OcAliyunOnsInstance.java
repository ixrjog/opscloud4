package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_aliyun_ons_instance")
public class OcAliyunOnsInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 实例id
     */
    @Column(name = "instance_id")
    private String instanceId;

    /**
     * 实例名称
     */
    @Column(name = "instance_name")
    private String instanceName;

    @Column(name = "env_name")
    private String envName;

    /**
     * 实例是否有命名空间。取值说明如下：
     * true：拥有独立命名空间，资源命名确保实例内唯一，跨实例之间可重名。
     * false：无独立命名空间，实例内或者跨实例之间，资源命名必须全局唯一。
     */
    @Column(name = "independent_naming")
    private Boolean independentNaming;

    /**
     * 实例状态。取值说明如下：
     * 0：铂金版实例部署中
     * 2：后付费实例已欠费
     * 5：后付费实例或铂金版实例服务中
     * 7：铂金版实例升级中且服务可用
     */
    @Column(name = "instance_status")
    private Integer instanceStatus;

    /**
     * 实例类型。取值说明如下：
     * 1：后付费实例
     * 2：铂金版实例
     */
    @Column(name = "instance_type")
    private Integer instanceType;

    /**
     * 地区id
     */
    @Column(name = "region_id")
    private String regionId;

    /**
     * 消息收发 TPS 上限
     */
    @Column(name = "max_tps")
    private Long maxTps;

    /**
     * 该实例下允许创建的 Topic 数量上限
     */
    @Column(name = "topic_capacity")
    private Integer topicCapacity;

    /**
     * 铂金版实例的过期时间
     */
    @Column(name = "release_time")
    private Long releaseTime;

    /**
     * 备注
     */
    private String remark;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * TCP 协议接入点
     */
    @Column(name = "tcp_endpoint")
    private String tcpEndpoint;

    /**
     * HTTP 公网接入点
     */
    @Column(name = "http_Internet_endpoint")
    private String httpInternetEndpoint;

    /**
     * HTTPS 公网接入点
     */
    @Column(name = "http_internet_secure_endpoint")
    private String httpInternetSecureEndpoint;

    /**
     * HTTP 内网接入点
     */
    @Column(name = "http_internal_endpoint")
    private String httpInternalEndpoint;

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
     * 获取实例id
     *
     * @return instance_id - 实例id
     */
    public String getInstanceId() {
        return instanceId;
    }

    /**
     * 设置实例id
     *
     * @param instanceId 实例id
     */
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    /**
     * 获取实例名称
     *
     * @return instance_name - 实例名称
     */
    public String getInstanceName() {
        return instanceName;
    }

    /**
     * 设置实例名称
     *
     * @param instanceName 实例名称
     */
    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    /**
     * @return env_name
     */
    public String getEnvName() {
        return envName;
    }

    /**
     * @param envName
     */
    public void setEnvName(String envName) {
        this.envName = envName;
    }

    /**
     * 获取实例是否有命名空间。取值说明如下：
     * true：拥有独立命名空间，资源命名确保实例内唯一，跨实例之间可重名。
     * false：无独立命名空间，实例内或者跨实例之间，资源命名必须全局唯一。
     *
     * @return independent_naming - 实例是否有命名空间。取值说明如下：
     * true：拥有独立命名空间，资源命名确保实例内唯一，跨实例之间可重名。
     * false：无独立命名空间，实例内或者跨实例之间，资源命名必须全局唯一。
     */
    public Boolean getIndependentNaming() {
        return independentNaming;
    }

    /**
     * 设置实例是否有命名空间。取值说明如下：
     * true：拥有独立命名空间，资源命名确保实例内唯一，跨实例之间可重名。
     * false：无独立命名空间，实例内或者跨实例之间，资源命名必须全局唯一。
     *
     * @param independentNaming 实例是否有命名空间。取值说明如下：
     *                          true：拥有独立命名空间，资源命名确保实例内唯一，跨实例之间可重名。
     *                          false：无独立命名空间，实例内或者跨实例之间，资源命名必须全局唯一。
     */
    public void setIndependentNaming(Boolean independentNaming) {
        this.independentNaming = independentNaming;
    }

    /**
     * 获取实例状态。取值说明如下：
     * 0：铂金版实例部署中
     * 2：后付费实例已欠费
     * 5：后付费实例或铂金版实例服务中
     * 7：铂金版实例升级中且服务可用
     *
     * @return instance_status - 实例状态。取值说明如下：
     * 0：铂金版实例部署中
     * 2：后付费实例已欠费
     * 5：后付费实例或铂金版实例服务中
     * 7：铂金版实例升级中且服务可用
     */
    public Integer getInstanceStatus() {
        return instanceStatus;
    }

    /**
     * 设置实例状态。取值说明如下：
     * 0：铂金版实例部署中
     * 2：后付费实例已欠费
     * 5：后付费实例或铂金版实例服务中
     * 7：铂金版实例升级中且服务可用
     *
     * @param instanceStatus 实例状态。取值说明如下：
     *                       0：铂金版实例部署中
     *                       2：后付费实例已欠费
     *                       5：后付费实例或铂金版实例服务中
     *                       7：铂金版实例升级中且服务可用
     */
    public void setInstanceStatus(Integer instanceStatus) {
        this.instanceStatus = instanceStatus;
    }

    /**
     * 获取实例类型。取值说明如下：
     * 1：后付费实例
     * 2：铂金版实例
     *
     * @return instance_type - 实例类型。取值说明如下：
     * 1：后付费实例
     * 2：铂金版实例
     */
    public Integer getInstanceType() {
        return instanceType;
    }

    /**
     * 设置实例类型。取值说明如下：
     * 1：后付费实例
     * 2：铂金版实例
     *
     * @param instanceType 实例类型。取值说明如下：
     *                     1：后付费实例
     *                     2：铂金版实例
     */
    public void setInstanceType(Integer instanceType) {
        this.instanceType = instanceType;
    }

    /**
     * 获取地区id
     *
     * @return region_id - 地区id
     */
    public String getRegionId() {
        return regionId;
    }

    /**
     * 设置地区id
     *
     * @param regionId 地区id
     */
    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    /**
     * 获取消息收发 TPS 上限
     *
     * @return max_tps - 消息收发 TPS 上限
     */
    public Long getMaxTps() {
        return maxTps;
    }

    /**
     * 设置消息收发 TPS 上限
     *
     * @param maxTps 消息收发 TPS 上限
     */
    public void setMaxTps(Long maxTps) {
        this.maxTps = maxTps;
    }

    /**
     * 获取该实例下允许创建的 Topic 数量上限
     *
     * @return topic_capacity - 该实例下允许创建的 Topic 数量上限
     */
    public Integer getTopicCapacity() {
        return topicCapacity;
    }

    /**
     * 设置该实例下允许创建的 Topic 数量上限
     *
     * @param topicCapacity 该实例下允许创建的 Topic 数量上限
     */
    public void setTopicCapacity(Integer topicCapacity) {
        this.topicCapacity = topicCapacity;
    }

    /**
     * 获取铂金版实例的过期时间
     *
     * @return release_time - 铂金版实例的过期时间
     */
    public Long getReleaseTime() {
        return releaseTime;
    }

    /**
     * 设置铂金版实例的过期时间
     *
     * @param releaseTime 铂金版实例的过期时间
     */
    public void setReleaseTime(Long releaseTime) {
        this.releaseTime = releaseTime;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
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
     * 获取TCP 协议接入点
     *
     * @return tcp_endpoint - TCP 协议接入点
     */
    public String getTcpEndpoint() {
        return tcpEndpoint;
    }

    /**
     * 设置TCP 协议接入点
     *
     * @param tcpEndpoint TCP 协议接入点
     */
    public void setTcpEndpoint(String tcpEndpoint) {
        this.tcpEndpoint = tcpEndpoint;
    }

    /**
     * 获取HTTP 公网接入点
     *
     * @return http_Internet_endpoint - HTTP 公网接入点
     */
    public String getHttpInternetEndpoint() {
        return httpInternetEndpoint;
    }

    /**
     * 设置HTTP 公网接入点
     *
     * @param httpInternetEndpoint HTTP 公网接入点
     */
    public void setHttpInternetEndpoint(String httpInternetEndpoint) {
        this.httpInternetEndpoint = httpInternetEndpoint;
    }

    /**
     * 获取HTTPS 公网接入点
     *
     * @return http_internet_secure_endpoint - HTTPS 公网接入点
     */
    public String getHttpInternetSecureEndpoint() {
        return httpInternetSecureEndpoint;
    }

    /**
     * 设置HTTPS 公网接入点
     *
     * @param httpInternetSecureEndpoint HTTPS 公网接入点
     */
    public void setHttpInternetSecureEndpoint(String httpInternetSecureEndpoint) {
        this.httpInternetSecureEndpoint = httpInternetSecureEndpoint;
    }

    /**
     * 获取HTTP 内网接入点
     *
     * @return http_internal_endpoint - HTTP 内网接入点
     */
    public String getHttpInternalEndpoint() {
        return httpInternalEndpoint;
    }

    /**
     * 设置HTTP 内网接入点
     *
     * @param httpInternalEndpoint HTTP 内网接入点
     */
    public void setHttpInternalEndpoint(String httpInternalEndpoint) {
        this.httpInternalEndpoint = httpInternalEndpoint;
    }
}