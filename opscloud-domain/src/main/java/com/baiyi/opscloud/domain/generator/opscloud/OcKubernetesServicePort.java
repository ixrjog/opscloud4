package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_kubernetes_service_port")
public class OcKubernetesServicePort {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 服务id
     */
    @Column(name = "service_id")
    private Integer serviceId;

    /**
     * 端口名称
     */
    private String name;

    /**
     * node端口
     */
    @Column(name = "node_port")
    private Integer nodePort;

    /**
     * 端口
     */
    private Integer port;

    /**
     * target端口
     */
    @Column(name = "target_port")
    private Integer targetPort;

    /**
     * 协议
     */
    private String protocol;

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
     * 获取服务id
     *
     * @return service_id - 服务id
     */
    public Integer getServiceId() {
        return serviceId;
    }

    /**
     * 设置服务id
     *
     * @param serviceId 服务id
     */
    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * 获取端口名称
     *
     * @return name - 端口名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置端口名称
     *
     * @param name 端口名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取node端口
     *
     * @return node_port - node端口
     */
    public Integer getNodePort() {
        return nodePort;
    }

    /**
     * 设置node端口
     *
     * @param nodePort node端口
     */
    public void setNodePort(Integer nodePort) {
        this.nodePort = nodePort;
    }

    /**
     * 获取端口
     *
     * @return port - 端口
     */
    public Integer getPort() {
        return port;
    }

    /**
     * 设置端口
     *
     * @param port 端口
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * 获取target端口
     *
     * @return target_port - target端口
     */
    public Integer getTargetPort() {
        return targetPort;
    }

    /**
     * 设置target端口
     *
     * @param targetPort target端口
     */
    public void setTargetPort(Integer targetPort) {
        this.targetPort = targetPort;
    }

    /**
     * 获取协议
     *
     * @return protocol - 协议
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * 设置协议
     *
     * @param protocol 协议
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
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
}