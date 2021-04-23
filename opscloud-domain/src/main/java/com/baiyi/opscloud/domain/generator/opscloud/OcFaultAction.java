package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_fault_action")
public class OcFaultAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 故障id
     */
    @Column(name = "fault_id")
    private Integer faultId;

    /**
     * 故障action
     */
    @Column(name = "fault_action")
    private String faultAction;

    /**
     * action跟进人
     */
    @Column(name = "follow_user_id")
    private Integer followUserId;

    /**
     * action状态
     * 0：完成
     * 1：待完成
     * 2：关闭
     */
    @Column(name = "action_status")
    private Integer actionStatus;

    /**
     * 截止时间
     */
    private Date deadline;

    @Column(name = "create_time",insertable = false,updatable = false)
    private Date createTime;

    @Column(name = "update_time",insertable = false,updatable = false)
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
     * 获取故障action
     *
     * @return fault_action - 故障action
     */
    public String getFaultAction() {
        return faultAction;
    }

    /**
     * 设置故障action
     *
     * @param faultAction 故障action
     */
    public void setFaultAction(String faultAction) {
        this.faultAction = faultAction;
    }

    /**
     * 获取action跟进人
     *
     * @return follow_user_id - action跟进人
     */
    public Integer getFollowUserId() {
        return followUserId;
    }

    /**
     * 设置action跟进人
     *
     * @param followUserId action跟进人
     */
    public void setFollowUserId(Integer followUserId) {
        this.followUserId = followUserId;
    }

    /**
     * 获取action状态
     * 0：完成
     * 1：待完成
     * 2：关闭
     *
     * @return action_status - action状态
     * 0：完成
     * 1：待完成
     * 2：关闭
     */
    public Integer getActionStatus() {
        return actionStatus;
    }

    /**
     * 设置action状态
     * 0：完成
     * 1：待完成
     * 2：关闭
     *
     * @param actionStatus action状态
     *                     0：完成
     *                     1：待完成
     *                     2：关闭
     */
    public void setActionStatus(Integer actionStatus) {
        this.actionStatus = actionStatus;
    }

    /**
     * 获取截止时间
     *
     * @return deadline - 截止时间
     */
    public Date getDeadline() {
        return deadline;
    }

    /**
     * 设置截止时间
     *
     * @param deadline 截止时间
     */
    public void setDeadline(Date deadline) {
        this.deadline = deadline;
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