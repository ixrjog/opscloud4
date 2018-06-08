package com.sdg.cmdb.domain.projectManagement;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class ProjectManagementDO implements Serializable {

    private static final long serialVersionUID = -8063102071540681488L;

    private long id;

    private String projectName;

    private String content;

    private String beginTime;

    private int projectType;

    private int ttl;

    private long leaderUserId;

    private String leaderUsername;

    // 项目状态
    private int status;

    private String gmtCreate;

    private String gmtModify;

    public ProjectManagementDO() {

    }

    public ProjectManagementDO(ProjectManagementVO pmVO) {
        this.id = pmVO.getId();
        this.projectName = pmVO.getProjectName();
        this.content = pmVO.getContent();
        this.beginTime = pmVO.getBeginTime();
        this.projectType = pmVO.getProjectType();
        this.ttl = pmVO.getTtl();

        if (!StringUtils.isEmpty(pmVO.getLeaderUsername())) {
            this.leaderUsername = pmVO.getLeaderUsername();
        }

        if (pmVO.getLeaderUserId() != 0) {
            this.leaderUserId = pmVO.getLeaderUserId();
        } else {
            if (pmVO.getLeaderUser() != null) {
                this.leaderUserId = pmVO.getLeaderUser().getId();
                this.leaderUsername = pmVO.getLeaderUsername();
            }
        }
    }

    @Override
    public String toString() {
        return "ProjectManagementDO{" +
                "id=" + id +
                ", projectName='" + projectName + '\'' +
                ", content='" + content + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", projectType=" + projectType +
                ", ttl=" + ttl +
                ", leaderUserId=" + leaderUserId +
                ", leaderUsername='" + leaderUsername + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                '}';
    }

    public enum ProjectTypeEnum {
        //0 保留／在组中代表的是所有权限
        longterm(0, "longterm"),
        shortterm(1, "shortterm");
        private int code;
        private String desc;

        ProjectTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getProjectTypeName(int code) {
            for (ProjectTypeEnum projectTypeEnum : ProjectTypeEnum.values()) {
                if (projectTypeEnum.getCode() == code) {
                    return projectTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

    public enum ProjectStatusEnum {
        //0 保留／在组中代表的是所有权限
        normal(0, "normal"),
        death(1, "death"),
        lose(2, "lose"),
        renew(3, "renew");
        private int code;
        private String desc;

        ProjectStatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getProjectStatusName(int code) {
            for (ProjectStatusEnum projectStatusEnum : ProjectStatusEnum.values()) {
                if (projectStatusEnum.getCode() == code) {
                    return projectStatusEnum.getDesc();
                }
            }
            return "undefined";
        }
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public int getProjectType() {
        return projectType;
    }

    public void setProjectType(int projectType) {
        this.projectType = projectType;
    }

    public long getLeaderUserId() {
        return leaderUserId;
    }

    public void setLeaderUserId(long leaderUserId) {
        this.leaderUserId = leaderUserId;
    }

    public String getLeaderUsername() {
        return leaderUsername;
    }

    public void setLeaderUsername(String leaderUsername) {
        this.leaderUsername = leaderUsername;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
