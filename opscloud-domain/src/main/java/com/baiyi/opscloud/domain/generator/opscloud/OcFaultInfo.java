package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_fault_info")
public class OcFaultInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 是否完成
     */
    private Boolean finalized;

    /**
     * 故障标题
     */
    @Column(name = "fault_title")
    private String faultTitle;

    /**
     * 故障级别
     */
    @Column(name = "fault_level")
    private String faultLevel;

    /**
     * root_cause_type
     */
    private String content;

    @Column(name = "root_cause_type_id")
    private Integer rootCauseTypeId;

    /**
     * 故障开始时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 故障结束时间
     */
    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 故障原因
     */
    @Column(name = "root_cause")
    private String rootCause;

    /**
     * 故障详细过程
     */
    @Column(name = "fault_detail")
    private String faultDetail;

    /**
     * 故障评级原因
     */
    @Column(name = "fault_judge")
    private String faultJudge;

    /**
     * 所属团队
     */
    @Column(name = "responsible_team")
    private String responsibleTeam;

    /**
     * 造成的影响
     */
    private String effect;

    /**
     * 现象故障产生的现象
     */
    @Column(name = "fault_performance")
    private String faultPerformance;

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
     * 获取是否完成
     *
     * @return finalized - 是否完成
     */
    public Boolean getFinalized() {
        return finalized;
    }

    /**
     * 设置是否完成
     *
     * @param finalized 是否完成
     */
    public void setFinalized(Boolean finalized) {
        this.finalized = finalized;
    }

    /**
     * 获取故障标题
     *
     * @return fault_title - 故障标题
     */
    public String getFaultTitle() {
        return faultTitle;
    }

    /**
     * 设置故障标题
     *
     * @param faultTitle 故障标题
     */
    public void setFaultTitle(String faultTitle) {
        this.faultTitle = faultTitle;
    }

    /**
     * 获取故障级别
     *
     * @return fault_level - 故障级别
     */
    public String getFaultLevel() {
        return faultLevel;
    }

    /**
     * 设置故障级别
     *
     * @param faultLevel 故障级别
     */
    public void setFaultLevel(String faultLevel) {
        this.faultLevel = faultLevel;
    }

    /**
     * 获取root_cause_type
     *
     * @return content - root_cause_type
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置root_cause_type
     *
     * @param content root_cause_type
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return root_cause_type_id
     */
    public Integer getRootCauseTypeId() {
        return rootCauseTypeId;
    }

    /**
     * @param rootCauseTypeId
     */
    public void setRootCauseTypeId(Integer rootCauseTypeId) {
        this.rootCauseTypeId = rootCauseTypeId;
    }

    /**
     * 获取故障开始时间
     *
     * @return start_time - 故障开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置故障开始时间
     *
     * @param startTime 故障开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取故障结束时间
     *
     * @return end_time - 故障结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置故障结束时间
     *
     * @param endTime 故障结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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
     * 获取故障原因
     *
     * @return root_cause - 故障原因
     */
    public String getRootCause() {
        return rootCause;
    }

    /**
     * 设置故障原因
     *
     * @param rootCause 故障原因
     */
    public void setRootCause(String rootCause) {
        this.rootCause = rootCause;
    }

    /**
     * 获取故障详细过程
     *
     * @return fault_detail - 故障详细过程
     */
    public String getFaultDetail() {
        return faultDetail;
    }

    /**
     * 设置故障详细过程
     *
     * @param faultDetail 故障详细过程
     */
    public void setFaultDetail(String faultDetail) {
        this.faultDetail = faultDetail;
    }

    /**
     * 获取故障评级原因
     *
     * @return fault_judge - 故障评级原因
     */
    public String getFaultJudge() {
        return faultJudge;
    }

    /**
     * 设置故障评级原因
     *
     * @param faultJudge 故障评级原因
     */
    public void setFaultJudge(String faultJudge) {
        this.faultJudge = faultJudge;
    }

    /**
     * 获取所属团队
     *
     * @return responsible_team - 所属团队
     */
    public String getResponsibleTeam() {
        return responsibleTeam;
    }

    /**
     * 设置所属团队
     *
     * @param responsibleTeam 所属团队
     */
    public void setResponsibleTeam(String responsibleTeam) {
        this.responsibleTeam = responsibleTeam;
    }

    /**
     * 获取造成的影响
     *
     * @return effect - 造成的影响
     */
    public String getEffect() {
        return effect;
    }

    /**
     * 设置造成的影响
     *
     * @param effect 造成的影响
     */
    public void setEffect(String effect) {
        this.effect = effect;
    }

    /**
     * 获取现象故障产生的现象
     *
     * @return fault_performance - 现象故障产生的现象
     */
    public String getFaultPerformance() {
        return faultPerformance;
    }

    /**
     * 设置现象故障产生的现象
     *
     * @param faultPerformance 现象故障产生的现象
     */
    public void setFaultPerformance(String faultPerformance) {
        this.faultPerformance = faultPerformance;
    }
}