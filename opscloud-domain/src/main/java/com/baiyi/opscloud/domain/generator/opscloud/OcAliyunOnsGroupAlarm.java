package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_aliyun_ons_group_alarm")
public class OcAliyunOnsGroupAlarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 地区id
     */
    @Column(name = "region_id")
    private String regionId;

    /**
     * oc_aliyun_ons_group 关联id
     */
    @Column(name = "ons_group_id")
    private Integer onsGroupId;

    /**
     * 告警状态
     * 0：停用
     * 1：启用
     * -1：异常
     */
    @Column(name = "alarm_status")
    private Integer alarmStatus;

    /**
     * 是否在线
     */
    private Boolean online;

    /**
     * 订阅关系是否一致
     */
    @Column(name = "subscription_same")
    private Boolean subscriptionSame;

    /**
     * rebalance是否正常
     */
    @Column(name = "rebalance_ok")
    private Boolean rebalanceOk;

    /**
     * 集群总消费堆积
     */
    @Column(name = "total_diff")
    private Long totalDiff;

    /**
     * 延迟时间
     */
    @Column(name = "delay_time")
    private Long delayTime;

    /**
     * 告警沉默时间，单位分钟
     */
    @Column(name = "alarm_silent_time")
    private Integer alarmSilentTime;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 告警用户列表
     */
    @Column(name = "alarm_user_list")
    private String alarmUserList;

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
     * 获取地区id
     *
     * @return region_id - 地区id
     */
    public String getRegionId() {
        return regionId;
    }

    /**
     * 设置地区id
     *
     * @param regionId 地区id
     */
    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    /**
     * 获取oc_aliyun_ons_group 关联id
     *
     * @return ons_group_id - oc_aliyun_ons_group 关联id
     */
    public Integer getOnsGroupId() {
        return onsGroupId;
    }

    /**
     * 设置oc_aliyun_ons_group 关联id
     *
     * @param onsGroupId oc_aliyun_ons_group 关联id
     */
    public void setOnsGroupId(Integer onsGroupId) {
        this.onsGroupId = onsGroupId;
    }

    /**
     * 获取告警状态
     * 0：停用
     * 1：启用
     * -1：异常
     *
     * @return alarm_status - 告警状态
     * 0：停用
     * 1：启用
     * -1：异常
     */
    public Integer getAlarmStatus() {
        return alarmStatus;
    }

    /**
     * 设置告警状态
     * 0：停用
     * 1：启用
     * -1：异常
     *
     * @param alarmStatus 告警状态
     *                    0：停用
     *                    1：启用
     *                    -1：异常
     */
    public void setAlarmStatus(Integer alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    /**
     * 获取是否在线
     *
     * @return online - 是否在线
     */
    public Boolean getOnline() {
        return online;
    }

    /**
     * 设置是否在线
     *
     * @param online 是否在线
     */
    public void setOnline(Boolean online) {
        this.online = online;
    }

    /**
     * 获取订阅关系是否一致
     *
     * @return subscription_same - 订阅关系是否一致
     */
    public Boolean getSubscriptionSame() {
        return subscriptionSame;
    }

    /**
     * 设置订阅关系是否一致
     *
     * @param subscriptionSame 订阅关系是否一致
     */
    public void setSubscriptionSame(Boolean subscriptionSame) {
        this.subscriptionSame = subscriptionSame;
    }

    /**
     * 获取rebalance是否正常
     *
     * @return rebalance_ok - rebalance是否正常
     */
    public Boolean getRebalanceOk() {
        return rebalanceOk;
    }

    /**
     * 设置rebalance是否正常
     *
     * @param rebalanceOk rebalance是否正常
     */
    public void setRebalanceOk(Boolean rebalanceOk) {
        this.rebalanceOk = rebalanceOk;
    }

    /**
     * 获取集群总消费堆积
     *
     * @return total_diff - 集群总消费堆积
     */
    public Long getTotalDiff() {
        return totalDiff;
    }

    /**
     * 设置集群总消费堆积
     *
     * @param totalDiff 集群总消费堆积
     */
    public void setTotalDiff(Long totalDiff) {
        this.totalDiff = totalDiff;
    }

    /**
     * 获取延迟时间
     *
     * @return delay_time - 延迟时间
     */
    public Long getDelayTime() {
        return delayTime;
    }

    /**
     * 设置延迟时间
     *
     * @param delayTime 延迟时间
     */
    public void setDelayTime(Long delayTime) {
        this.delayTime = delayTime;
    }

    /**
     * 获取告警沉默时间，单位分钟
     *
     * @return alarm_silent_time - 告警沉默时间，单位分钟
     */
    public Integer getAlarmSilentTime() {
        return alarmSilentTime;
    }

    /**
     * 设置告警沉默时间，单位分钟
     *
     * @param alarmSilentTime 告警沉默时间，单位分钟
     */
    public void setAlarmSilentTime(Integer alarmSilentTime) {
        this.alarmSilentTime = alarmSilentTime;
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

    /**
     * 获取告警用户列表
     *
     * @return alarm_user_list - 告警用户列表
     */
    public String getAlarmUserList() {
        return alarmUserList;
    }

    /**
     * 设置告警用户列表
     *
     * @param alarmUserList 告警用户列表
     */
    public void setAlarmUserList(String alarmUserList) {
        this.alarmUserList = alarmUserList;
    }
}