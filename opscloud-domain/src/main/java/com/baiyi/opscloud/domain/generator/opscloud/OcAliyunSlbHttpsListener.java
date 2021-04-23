package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_aliyun_slb_https_listener")
public class OcAliyunSlbHttpsListener {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 负载均衡实例的ID
     */
    @Column(name = "load_balancer_id")
    private String loadBalancerId;

    /**
     * 负载均衡实例的名称
     */
    @Column(name = "load_balancer_name")
    private String loadBalancerName;

    /**
     * 负载均衡实例https监听前端使用的端口
     */
    @Column(name = "https_listener_port")
    private Integer httpsListenerPort;

    /**
     * 服务器证书的ID
     */
    @Column(name = "server_certificate_id")
    private String serverCertificateId;

    /**
     * 服务器证书的域名
     */
    @Column(name = "server_certificate_domain")
    private String serverCertificateDomain;

    /**
     * 服务器证书类型
     * 1: default：默认
     * 2: extension:  拓展
     */
    @Column(name = "server_certificate_type")
    private Integer serverCertificateType;

    /**
     * 域名扩展ID
     */
    @Column(name = "domain_extension_id")
    private String domainExtensionId;

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
     * 获取负载均衡实例的ID
     *
     * @return load_balancer_id - 负载均衡实例的ID
     */
    public String getLoadBalancerId() {
        return loadBalancerId;
    }

    /**
     * 设置负载均衡实例的ID
     *
     * @param loadBalancerId 负载均衡实例的ID
     */
    public void setLoadBalancerId(String loadBalancerId) {
        this.loadBalancerId = loadBalancerId;
    }

    /**
     * 获取负载均衡实例的名称
     *
     * @return load_balancer_name - 负载均衡实例的名称
     */
    public String getLoadBalancerName() {
        return loadBalancerName;
    }

    /**
     * 设置负载均衡实例的名称
     *
     * @param loadBalancerName 负载均衡实例的名称
     */
    public void setLoadBalancerName(String loadBalancerName) {
        this.loadBalancerName = loadBalancerName;
    }

    /**
     * 获取负载均衡实例https监听前端使用的端口
     *
     * @return https_listener_port - 负载均衡实例https监听前端使用的端口
     */
    public Integer getHttpsListenerPort() {
        return httpsListenerPort;
    }

    /**
     * 设置负载均衡实例https监听前端使用的端口
     *
     * @param httpsListenerPort 负载均衡实例https监听前端使用的端口
     */
    public void setHttpsListenerPort(Integer httpsListenerPort) {
        this.httpsListenerPort = httpsListenerPort;
    }

    /**
     * 获取服务器证书的ID
     *
     * @return server_certificate_id - 服务器证书的ID
     */
    public String getServerCertificateId() {
        return serverCertificateId;
    }

    /**
     * 设置服务器证书的ID
     *
     * @param serverCertificateId 服务器证书的ID
     */
    public void setServerCertificateId(String serverCertificateId) {
        this.serverCertificateId = serverCertificateId;
    }

    /**
     * 获取服务器证书的域名
     *
     * @return server_certificate_domain - 服务器证书的域名
     */
    public String getServerCertificateDomain() {
        return serverCertificateDomain;
    }

    /**
     * 设置服务器证书的域名
     *
     * @param serverCertificateDomain 服务器证书的域名
     */
    public void setServerCertificateDomain(String serverCertificateDomain) {
        this.serverCertificateDomain = serverCertificateDomain;
    }

    /**
     * 获取服务器证书类型
     * 1: default：默认
     * 2: extension:  拓展
     *
     * @return server_certificate_type - 服务器证书类型
     * 1: default：默认
     * 2: extension:  拓展
     */
    public Integer getServerCertificateType() {
        return serverCertificateType;
    }

    /**
     * 设置服务器证书类型
     * 1: default：默认
     * 2: extension:  拓展
     *
     * @param serverCertificateType 服务器证书类型
     *                              1: default：默认
     *                              2: extension:  拓展
     */
    public void setServerCertificateType(Integer serverCertificateType) {
        this.serverCertificateType = serverCertificateType;
    }

    /**
     * 获取域名扩展ID
     *
     * @return domain_extension_id - 域名扩展ID
     */
    public String getDomainExtensionId() {
        return domainExtensionId;
    }

    /**
     * 设置域名扩展ID
     *
     * @param domainExtensionId 域名扩展ID
     */
    public void setDomainExtensionId(String domainExtensionId) {
        this.domainExtensionId = domainExtensionId;
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