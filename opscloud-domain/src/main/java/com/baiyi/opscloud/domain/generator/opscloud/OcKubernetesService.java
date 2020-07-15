package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_kubernetes_service")
public class OcKubernetesService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 应用id
     */
    @Column(name = "application_id")
    private Integer applicationId;

    /**
     * 应用实例id
     */
    @Column(name = "instance_id")
    private Integer instanceId;

    @Column(name = "namespace_id")
    private Integer namespaceId;

    /**
     * 命名空间
     */
    private String namespace;

    /**
     * 名称
     */
    private String name;

    /**
     * 集群ip
     */
    @Column(name = "cluster_ip")
    private String clusterIp;

    /**
     * 服务类型
     */
    @Column(name = "service_type")
    private String serviceType;

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

    @Column(name = "service_yaml")
    private String serviceYaml;

    @Column(name = "service_detail")
    private String serviceDetail;

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
     * 获取应用id
     *
     * @return application_id - 应用id
     */
    public Integer getApplicationId() {
        return applicationId;
    }

    /**
     * 设置应用id
     *
     * @param applicationId 应用id
     */
    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * 获取应用实例id
     *
     * @return instance_id - 应用实例id
     */
    public Integer getInstanceId() {
        return instanceId;
    }

    /**
     * 设置应用实例id
     *
     * @param instanceId 应用实例id
     */
    public void setInstanceId(Integer instanceId) {
        this.instanceId = instanceId;
    }

    /**
     * @return namespace_id
     */
    public Integer getNamespaceId() {
        return namespaceId;
    }

    /**
     * @param namespaceId
     */
    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    /**
     * 获取命名空间
     *
     * @return namespace - 命名空间
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * 设置命名空间
     *
     * @param namespace 命名空间
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
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
     * 获取集群ip
     *
     * @return cluster_ip - 集群ip
     */
    public String getClusterIp() {
        return clusterIp;
    }

    /**
     * 设置集群ip
     *
     * @param clusterIp 集群ip
     */
    public void setClusterIp(String clusterIp) {
        this.clusterIp = clusterIp;
    }

    /**
     * 获取服务类型
     *
     * @return service_type - 服务类型
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * 设置服务类型
     *
     * @param serviceType 服务类型
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
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
     * @return service_yaml
     */
    public String getServiceYaml() {
        return serviceYaml;
    }

    /**
     * @param serviceYaml
     */
    public void setServiceYaml(String serviceYaml) {
        this.serviceYaml = serviceYaml;
    }

    /**
     * @return service_detail
     */
    public String getServiceDetail() {
        return serviceDetail;
    }

    /**
     * @param serviceDetail
     */
    public void setServiceDetail(String serviceDetail) {
        this.serviceDetail = serviceDetail;
    }
}