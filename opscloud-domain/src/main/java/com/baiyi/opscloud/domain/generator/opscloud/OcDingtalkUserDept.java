package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_dingtalk_user_dept")
public class OcDingtalkUserDept {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "oc_dingtalk_dept_id")
    private Integer ocDingtalkDeptId;

    @Column(name = "oc_account_username")
    private String ocAccountUsername;

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
     * @return oc_dingtalk_dept_id
     */
    public Integer getOcDingtalkDeptId() {
        return ocDingtalkDeptId;
    }

    /**
     * @param ocDingtalkDeptId
     */
    public void setOcDingtalkDeptId(Integer ocDingtalkDeptId) {
        this.ocDingtalkDeptId = ocDingtalkDeptId;
    }

    /**
     * @return oc_account_username
     */
    public String getOcAccountUsername() {
        return ocAccountUsername;
    }

    /**
     * @param ocAccountUsername
     */
    public void setOcAccountUsername(String ocAccountUsername) {
        this.ocAccountUsername = ocAccountUsername;
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