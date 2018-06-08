package com.sdg.cmdb.domain.jenkins;

import com.sdg.cmdb.domain.auth.UserDO;

import java.io.Serializable;

public class JobUserDO implements Serializable {
    private static final long serialVersionUID = 6503856398114815618L;

    public static final int EMAIL_USER = 0;

    private long id;

    private long jobId;

    private long userId;

    public JobUserDO() {

    }

    public JobUserDO(UserDO userDO, long jobId, int userType) {
        this.userId = userDO.getId();
        this.jobId = jobId;
        this.userType = userType;
    }

    // 0 EMAIL_USER 邮件通知用户
    private int userType;

    private String gmtCreate;

    private String gmtModify;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }
}
