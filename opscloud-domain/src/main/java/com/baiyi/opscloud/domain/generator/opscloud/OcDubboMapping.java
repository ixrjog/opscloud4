package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_dubbo_mapping")
public class OcDubboMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 环境名称
     */
    private String env;

    /**
     * dubbo接口路径
     */
    @Column(name = "dubbo_interface")
    private String dubboInterface;

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
     * 获取环境名称
     *
     * @return env - 环境名称
     */
    public String getEnv() {
        return env;
    }

    /**
     * 设置环境名称
     *
     * @param env 环境名称
     */
    public void setEnv(String env) {
        this.env = env;
    }

    /**
     * 获取dubbo接口路径
     *
     * @return dubbo_interface - dubbo接口路径
     */
    public String getDubboInterface() {
        return dubboInterface;
    }

    /**
     * 设置dubbo接口路径
     *
     * @param dubboInterface dubbo接口路径
     */
    public void setDubboInterface(String dubboInterface) {
        this.dubboInterface = dubboInterface;
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