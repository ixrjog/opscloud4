package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_dubbo_mapping_server")
public class OcDubboMappingServer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "mapping_id")
    private Integer mappingId;

    /**
     * 服务器id
     */
    @Column(name = "server_id")
    private Integer serverId;

    /**
     * 服务器名称
     */
    @Column(name = "server_name")
    private String serverName;

    /**
     * k8s pod ip
     */
    private String ip;

    /**
     * dubbo端口
     */
    @Column(name = "dubbo_port")
    private Integer dubboPort;

    /**
     * 服务端口
     */
    @Column(name = "server_port")
    private Integer serverPort;

    @Column(name = "tcp_mapping_id")
    private Integer tcpMappingId;

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
     * @return mapping_id
     */
    public Integer getMappingId() {
        return mappingId;
    }

    /**
     * @param mappingId
     */
    public void setMappingId(Integer mappingId) {
        this.mappingId = mappingId;
    }

    /**
     * 获取服务器id
     *
     * @return server_id - 服务器id
     */
    public Integer getServerId() {
        return serverId;
    }

    /**
     * 设置服务器id
     *
     * @param serverId 服务器id
     */
    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    /**
     * 获取服务器名称
     *
     * @return server_name - 服务器名称
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * 设置服务器名称
     *
     * @param serverName 服务器名称
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * 获取k8s pod ip
     *
     * @return ip - k8s pod ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * 设置k8s pod ip
     *
     * @param ip k8s pod ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 获取dubbo端口
     *
     * @return dubbo_port - dubbo端口
     */
    public Integer getDubboPort() {
        return dubboPort;
    }

    /**
     * 设置dubbo端口
     *
     * @param dubboPort dubbo端口
     */
    public void setDubboPort(Integer dubboPort) {
        this.dubboPort = dubboPort;
    }

    /**
     * 获取服务端口
     *
     * @return server_port - 服务端口
     */
    public Integer getServerPort() {
        return serverPort;
    }

    /**
     * 设置服务端口
     *
     * @param serverPort 服务端口
     */
    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * @return tcp_mapping_id
     */
    public Integer getTcpMappingId() {
        return tcpMappingId;
    }

    /**
     * @param tcpMappingId
     */
    public void setTcpMappingId(Integer tcpMappingId) {
        this.tcpMappingId = tcpMappingId;
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
}