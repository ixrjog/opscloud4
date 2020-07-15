package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_kubernetes_cluster")
public class OcKubernetesCluster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 集群名称
     */
    private String name;

    /**
     * 管理url
     */
    @Column(name = "master_url")
    private String masterUrl;

    /**
     * 环境类型
     */
    @Column(name = "env_type")
    private Integer envType;

    @Column(name = "server_group_id")
    private Integer serverGroupId;

    @Column(name = "server_group_name")
    private String serverGroupName;

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

    private String comment;

    /**
     * 配置文件
     */
    private String kubeconfig;

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
     * 获取集群名称
     *
     * @return name - 集群名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置集群名称
     *
     * @param name 集群名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取管理url
     *
     * @return master_url - 管理url
     */
    public String getMasterUrl() {
        return masterUrl;
    }

    /**
     * 设置管理url
     *
     * @param masterUrl 管理url
     */
    public void setMasterUrl(String masterUrl) {
        this.masterUrl = masterUrl;
    }

    /**
     * 获取环境类型
     *
     * @return env_type - 环境类型
     */
    public Integer getEnvType() {
        return envType;
    }

    /**
     * 设置环境类型
     *
     * @param envType 环境类型
     */
    public void setEnvType(Integer envType) {
        this.envType = envType;
    }

    /**
     * @return server_group_id
     */
    public Integer getServerGroupId() {
        return serverGroupId;
    }

    /**
     * @param serverGroupId
     */
    public void setServerGroupId(Integer serverGroupId) {
        this.serverGroupId = serverGroupId;
    }

    /**
     * @return server_group_name
     */
    public String getServerGroupName() {
        return serverGroupName;
    }

    /**
     * @param serverGroupName
     */
    public void setServerGroupName(String serverGroupName) {
        this.serverGroupName = serverGroupName;
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
     * 获取配置文件
     *
     * @return kubeconfig - 配置文件
     */
    public String getKubeconfig() {
        return kubeconfig;
    }

    /**
     * 设置配置文件
     *
     * @param kubeconfig 配置文件
     */
    public void setKubeconfig(String kubeconfig) {
        this.kubeconfig = kubeconfig;
    }
}