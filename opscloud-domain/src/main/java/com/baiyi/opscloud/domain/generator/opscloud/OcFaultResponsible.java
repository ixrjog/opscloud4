package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_fault_responsible")
public class OcFaultResponsible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 故障id
     */
    @Column(name = "fault_id")
    private Integer faultId;

    /**
     * 责任人
     */
    @Column(name = "responsible_person")
    private Integer responsiblePerson;

    /**
     * 责任类型
     * 1：主要责任人
     * 2：次要责任人
     */
    @Column(name = "responsible_type")
    private Integer responsibleType;

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
     * 获取故障id
     *
     * @return fault_id - 故障id
     */
    public Integer getFaultId() {
        return faultId;
    }

    /**
     * 设置故障id
     *
     * @param faultId 故障id
     */
    public void setFaultId(Integer faultId) {
        this.faultId = faultId;
    }

    /**
     * 获取责任人
     *
     * @return responsible_person - 责任人
     */
    public Integer getResponsiblePerson() {
        return responsiblePerson;
    }

    /**
     * 设置责任人
     *
     * @param responsiblePerson 责任人
     */
    public void setResponsiblePerson(Integer responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    /**
     * 获取责任类型
     * 1：主要责任人
     * 2：次要责任人
     *
     * @return responsible_type - 责任类型
     * 1：主要责任人
     * 2：次要责任人
     */
    public Integer getResponsibleType() {
        return responsibleType;
    }

    /**
     * 设置责任类型
     * 1：主要责任人
     * 2：次要责任人
     *
     * @param responsibleType 责任类型
     *                        1：主要责任人
     *                        2：次要责任人
     */
    public void setResponsibleType(Integer responsibleType) {
        this.responsibleType = responsibleType;
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