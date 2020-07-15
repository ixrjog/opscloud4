package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_kubernetes_deployment")
public class OcKubernetesDeployment {
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
     * 标签
     */
    @Column(name = "label_app")
    private String labelApp;

    /**
     * 可用容器组数量
     */
    @Column(name = "available_replicas")
    private Integer availableReplicas;

    /**
     * 容器组数量
     */
    private Integer replicas;

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

    @Column(name = "deployment_detail")
    private String deploymentDetail;

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
     * 获取标签
     *
     * @return label_app - 标签
     */
    public String getLabelApp() {
        return labelApp;
    }

    /**
     * 设置标签
     *
     * @param labelApp 标签
     */
    public void setLabelApp(String labelApp) {
        this.labelApp = labelApp;
    }

    /**
     * 获取可用容器组数量
     *
     * @return available_replicas - 可用容器组数量
     */
    public Integer getAvailableReplicas() {
        return availableReplicas;
    }

    /**
     * 设置可用容器组数量
     *
     * @param availableReplicas 可用容器组数量
     */
    public void setAvailableReplicas(Integer availableReplicas) {
        this.availableReplicas = availableReplicas;
    }

    /**
     * 获取容器组数量
     *
     * @return replicas - 容器组数量
     */
    public Integer getReplicas() {
        return replicas;
    }

    /**
     * 设置容器组数量
     *
     * @param replicas 容器组数量
     */
    public void setReplicas(Integer replicas) {
        this.replicas = replicas;
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
     * @return deployment_detail
     */
    public String getDeploymentDetail() {
        return deploymentDetail;
    }

    /**
     * @param deploymentDetail
     */
    public void setDeploymentDetail(String deploymentDetail) {
        this.deploymentDetail = deploymentDetail;
    }
}