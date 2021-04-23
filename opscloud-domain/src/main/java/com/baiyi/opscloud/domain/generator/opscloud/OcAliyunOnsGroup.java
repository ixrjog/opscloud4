package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_aliyun_ons_group")
public class OcAliyunOnsGroup {
    /**
     * 实例id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "instance_id")
    private String instanceId;

    @Column(name = "group_id")
    private String groupId;

    /**
     * 实例是否有命名空间
     */
    @Column(name = "independent_naming")
    private Boolean independentNaming;

    /**
     * 查询的 Group ID 适用的协议。TCP 协议和 HTTP 协议的 Group ID 不可以共用，需要分别创建。取值说明如下：
     * tcp：表示该 Group ID 仅适用于 TCP 协议的消息收发。
     * http：表示该 Group ID 仅适用于 HTTP 协议的消息收发。
     */
    @Column(name = "group_type")
    private String groupType;

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
     * 获取实例id
     *
     * @return id - 实例id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置实例id
     *
     * @param id 实例id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return instance_id
     */
    public String getInstanceId() {
        return instanceId;
    }

    /**
     * @param instanceId
     */
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    /**
     * @return group_id
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @param groupId
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
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
     * 获取查询的 Group ID 适用的协议。TCP 协议和 HTTP 协议的 Group ID 不可以共用，需要分别创建。取值说明如下：
     * tcp：表示该 Group ID 仅适用于 TCP 协议的消息收发。
     * http：表示该 Group ID 仅适用于 HTTP 协议的消息收发。
     *
     * @return group_type - 查询的 Group ID 适用的协议。TCP 协议和 HTTP 协议的 Group ID 不可以共用，需要分别创建。取值说明如下：
     * tcp：表示该 Group ID 仅适用于 TCP 协议的消息收发。
     * http：表示该 Group ID 仅适用于 HTTP 协议的消息收发。
     */
    public String getGroupType() {
        return groupType;
    }

    /**
     * 设置查询的 Group ID 适用的协议。TCP 协议和 HTTP 协议的 Group ID 不可以共用，需要分别创建。取值说明如下：
     * tcp：表示该 Group ID 仅适用于 TCP 协议的消息收发。
     * http：表示该 Group ID 仅适用于 HTTP 协议的消息收发。
     *
     * @param groupType 查询的 Group ID 适用的协议。TCP 协议和 HTTP 协议的 Group ID 不可以共用，需要分别创建。取值说明如下：
     *                  tcp：表示该 Group ID 仅适用于 TCP 协议的消息收发。
     *                  http：表示该 Group ID 仅适用于 HTTP 协议的消息收发。
     */
    public void setGroupType(String groupType) {
        this.groupType = groupType;
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