package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_aliyun_slb_acl_listener")
public class OcAliyunSlbAclListener {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 控制策略组ID
     */
    @Column(name = "slb_acl_id")
    private String slbAclId;

    @Column(name = "slb_acl_name")
    private String slbAclName;

    /**
     * 访问控制的类型
     * black：黑名单
     * white：白名单
     */
    @Column(name = "slb_acl_type")
    private String slbAclType;

    /**
     * 绑定的监听的前端端口
     */
    @Column(name = "slb_acl_listener_port")
    private Integer slbAclListenerPort;

    /**
     * 绑定的监听的协议类型
     */
    @Column(name = "slb_acl_protocol")
    private String slbAclProtocol;

    /**
     * 负载均衡实例的ID
     */
    @Column(name = "load_balancer_id")
    private String loadBalancerId;

    @Column(name = "load_balancer_name")
    private String loadBalancerName;

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
     * 获取控制策略组ID
     *
     * @return slb_acl_id - 控制策略组ID
     */
    public String getSlbAclId() {
        return slbAclId;
    }

    /**
     * 设置控制策略组ID
     *
     * @param slbAclId 控制策略组ID
     */
    public void setSlbAclId(String slbAclId) {
        this.slbAclId = slbAclId;
    }

    /**
     * @return slb_acl_name
     */
    public String getSlbAclName() {
        return slbAclName;
    }

    /**
     * @param slbAclName
     */
    public void setSlbAclName(String slbAclName) {
        this.slbAclName = slbAclName;
    }

    /**
     * 获取访问控制的类型
     * black：黑名单
     * white：白名单
     *
     * @return slb_acl_type - 访问控制的类型
     * black：黑名单
     * white：白名单
     */
    public String getSlbAclType() {
        return slbAclType;
    }

    /**
     * 设置访问控制的类型
     * black：黑名单
     * white：白名单
     *
     * @param slbAclType 访问控制的类型
     *                   black：黑名单
     *                   white：白名单
     */
    public void setSlbAclType(String slbAclType) {
        this.slbAclType = slbAclType;
    }

    /**
     * 获取绑定的监听的前端端口
     *
     * @return slb_acl_listener_port - 绑定的监听的前端端口
     */
    public Integer getSlbAclListenerPort() {
        return slbAclListenerPort;
    }

    /**
     * 设置绑定的监听的前端端口
     *
     * @param slbAclListenerPort 绑定的监听的前端端口
     */
    public void setSlbAclListenerPort(Integer slbAclListenerPort) {
        this.slbAclListenerPort = slbAclListenerPort;
    }

    /**
     * 获取绑定的监听的协议类型
     *
     * @return slb_acl_protocol - 绑定的监听的协议类型
     */
    public String getSlbAclProtocol() {
        return slbAclProtocol;
    }

    /**
     * 设置绑定的监听的协议类型
     *
     * @param slbAclProtocol 绑定的监听的协议类型
     */
    public void setSlbAclProtocol(String slbAclProtocol) {
        this.slbAclProtocol = slbAclProtocol;
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
     * @return load_balancer_name
     */
    public String getLoadBalancerName() {
        return loadBalancerName;
    }

    /**
     * @param loadBalancerName
     */
    public void setLoadBalancerName(String loadBalancerName) {
        this.loadBalancerName = loadBalancerName;
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