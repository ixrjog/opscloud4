package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_aliyun_ons_topic")
public class OcAliyunOnsTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 实例id
     */
    @Column(name = "instance_id")
    private String instanceId;

    private String topic;

    /**
     * 实例是否有命名空间
     */
    @Column(name = "independent_naming")
    private Boolean independentNaming;

    /**
     * 消息类型。取值说明如下：
     * 0：普通消息
     * 1：分区顺序消息
     * 2：全局顺序消息
     * 4：事务消息
     * 5：定时/延时消息
     */
    @Column(name = "message_type")
    private Integer messageType;

    /**
     * 所有关系编号。取值说明如下：
     * 1：持有者
     * 2：可以发布
     * 4：可以订阅
     * 6：可以发布和订阅
     */
    private Integer relation;

    @Column(name = "relation_name")
    private String relationName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Long createTime;

    /**
     * 备注
     */
    private String remark;

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
     * 获取实例id
     *
     * @return instance_id - 实例id
     */
    public String getInstanceId() {
        return instanceId;
    }

    /**
     * 设置实例id
     *
     * @param instanceId 实例id
     */
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    /**
     * @return topic
     */
    public String getTopic() {
        return topic;
    }

    /**
     * @param topic
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * 获取实例是否有命名空间
     *
     * @return independent_naming - 实例是否有命名空间
     */
    public Boolean getIndependentNaming() {
        return independentNaming;
    }

    /**
     * 设置实例是否有命名空间
     *
     * @param independentNaming 实例是否有命名空间
     */
    public void setIndependentNaming(Boolean independentNaming) {
        this.independentNaming = independentNaming;
    }

    /**
     * 获取消息类型。取值说明如下：
     * 0：普通消息
     * 1：分区顺序消息
     * 2：全局顺序消息
     * 4：事务消息
     * 5：定时/延时消息
     *
     * @return message_type - 消息类型。取值说明如下：
     * 0：普通消息
     * 1：分区顺序消息
     * 2：全局顺序消息
     * 4：事务消息
     * 5：定时/延时消息
     */
    public Integer getMessageType() {
        return messageType;
    }

    /**
     * 设置消息类型。取值说明如下：
     * 0：普通消息
     * 1：分区顺序消息
     * 2：全局顺序消息
     * 4：事务消息
     * 5：定时/延时消息
     *
     * @param messageType 消息类型。取值说明如下：
     *                    0：普通消息
     *                    1：分区顺序消息
     *                    2：全局顺序消息
     *                    4：事务消息
     *                    5：定时/延时消息
     */
    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    /**
     * 获取所有关系编号。取值说明如下：
     * 1：持有者
     * 2：可以发布
     * 4：可以订阅
     * 6：可以发布和订阅
     *
     * @return relation - 所有关系编号。取值说明如下：
     * 1：持有者
     * 2：可以发布
     * 4：可以订阅
     * 6：可以发布和订阅
     */
    public Integer getRelation() {
        return relation;
    }

    /**
     * 设置所有关系编号。取值说明如下：
     * 1：持有者
     * 2：可以发布
     * 4：可以订阅
     * 6：可以发布和订阅
     *
     * @param relation 所有关系编号。取值说明如下：
     *                 1：持有者
     *                 2：可以发布
     *                 4：可以订阅
     *                 6：可以发布和订阅
     */
    public void setRelation(Integer relation) {
        this.relation = relation;
    }

    /**
     * @return relation_name
     */
    public String getRelationName() {
        return relationName;
    }

    /**
     * @param relationName
     */
    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
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