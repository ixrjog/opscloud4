package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_aliyun_slb_listener")
public class OcAliyunSlbListener {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 负载均衡实例的id
     */
    @Column(name = "load_balancer_id")
    private String loadBalancerId;

    /**
     * 负载均衡实例前端使用的端口
     */
    @Column(name = "listener_port")
    private Integer listenerPort;

    /**
     * 负载均衡实例前端使用的协议
     */
    @Column(name = "listener_protocol")
    private String listenerProtocol;

    /**
     * 是否启用监听转发
     */
    @Column(name = "listener_forward")
    private String listenerForward;

    /**
     * 转发到的目的监听端口，必须是已经存在的HTTPS监听端口。
     */
    @Column(name = "forward_port")
    private Integer forwardPort;

    /**
     * 负载均衡监听端口和协议描述
     */
    @Column(name = "listen_description")
    private String listenDescription;

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
     * 获取负载均衡实例的id
     *
     * @return load_balancer_id - 负载均衡实例的id
     */
    public String getLoadBalancerId() {
        return loadBalancerId;
    }

    /**
     * 设置负载均衡实例的id
     *
     * @param loadBalancerId 负载均衡实例的id
     */
    public void setLoadBalancerId(String loadBalancerId) {
        this.loadBalancerId = loadBalancerId;
    }

    /**
     * 获取负载均衡实例前端使用的端口
     *
     * @return listener_port - 负载均衡实例前端使用的端口
     */
    public Integer getListenerPort() {
        return listenerPort;
    }

    /**
     * 设置负载均衡实例前端使用的端口
     *
     * @param listenerPort 负载均衡实例前端使用的端口
     */
    public void setListenerPort(Integer listenerPort) {
        this.listenerPort = listenerPort;
    }

    /**
     * 获取负载均衡实例前端使用的协议
     *
     * @return listener_protocol - 负载均衡实例前端使用的协议
     */
    public String getListenerProtocol() {
        return listenerProtocol;
    }

    /**
     * 设置负载均衡实例前端使用的协议
     *
     * @param listenerProtocol 负载均衡实例前端使用的协议
     */
    public void setListenerProtocol(String listenerProtocol) {
        this.listenerProtocol = listenerProtocol;
    }

    /**
     * 获取是否启用监听转发
     *
     * @return listener_forward - 是否启用监听转发
     */
    public String getListenerForward() {
        return listenerForward;
    }

    /**
     * 设置是否启用监听转发
     *
     * @param listenerForward 是否启用监听转发
     */
    public void setListenerForward(String listenerForward) {
        this.listenerForward = listenerForward;
    }

    /**
     * 获取转发到的目的监听端口，必须是已经存在的HTTPS监听端口。
     *
     * @return forward_port - 转发到的目的监听端口，必须是已经存在的HTTPS监听端口。
     */
    public Integer getForwardPort() {
        return forwardPort;
    }

    /**
     * 设置转发到的目的监听端口，必须是已经存在的HTTPS监听端口。
     *
     * @param forwardPort 转发到的目的监听端口，必须是已经存在的HTTPS监听端口。
     */
    public void setForwardPort(Integer forwardPort) {
        this.forwardPort = forwardPort;
    }

    /**
     * 获取负载均衡监听端口和协议描述
     *
     * @return listen_description - 负载均衡监听端口和协议描述
     */
    public String getListenDescription() {
        return listenDescription;
    }

    /**
     * 设置负载均衡监听端口和协议描述
     *
     * @param listenDescription 负载均衡监听端口和协议描述
     */
    public void setListenDescription(String listenDescription) {
        this.listenDescription = listenDescription;
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