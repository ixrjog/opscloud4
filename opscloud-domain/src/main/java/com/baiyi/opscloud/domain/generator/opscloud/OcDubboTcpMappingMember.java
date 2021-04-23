package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_dubbo_tcp_mapping_member")
public class OcDubboTcpMappingMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tcp_mapping_id")
    private Integer tcpMappingId;

    @Column(name = "mapping_id")
    private Integer mappingId;

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