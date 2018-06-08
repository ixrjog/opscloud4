package com.sdg.cmdb.domain.ci;

import com.sdg.cmdb.domain.auth.UserVO;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by liangjian on 2017/1/21.
 */
public class CiDeployStatisticsDO implements Serializable {

    private static final long serialVersionUID = 3675574630886143286L;

    private long id;

    private String projectName;

    private String groupName;

    private String env;

    /**
     * 停用主机监控 (开始升级)
     * hostStatusDisable = 1;
     * 升级完毕
     * nhostStatusEnable = 0;
     * 启用主机监控（完成）
     * 2
     */
    private int status;

    private long deployId;

    /**
     * 部署类型
     */
    private int deployType = 0;

    // 版本号  eg:release-28
    private String bambooDeployVersion;

    private int bambooBuildNumber;

    private String bambooDeployProject;

    // 是否回滚操作
    private boolean bambooDeployRollback;

    // 操作人
    private String bambooManualBuildTriggerReasonUserName;

    // 0部署成功  1部署失败
    private int errorCode;

    private String branchName;

    private String gmtCreate;

    private String gmtModify;

    private UserVO userVO;

    public CiDeployStatisticsDO() {
    }

    public CiDeployStatisticsDO(String projectName, String groupName, String env, int status, long deployId, String bambooDeployVersion, int bambooBuildNumber, String bambooDeployProject,
                                boolean bambooDeployRollback, String bambooManualBuildTriggerReasonUserName, int errorCode, String branchName, int deployType
    ) {
        this.projectName = projectName;
        this.groupName = groupName;
        this.env = env;
        this.status = status;
        this.deployId = deployId;
        this.bambooDeployVersion = bambooDeployVersion;
        this.bambooBuildNumber = bambooBuildNumber;
        this.bambooDeployProject = bambooDeployProject;
        this.bambooDeployRollback = bambooDeployRollback;
        this.bambooManualBuildTriggerReasonUserName = bambooManualBuildTriggerReasonUserName;
        this.errorCode = errorCode;
        this.branchName = branchName;
        this.deployType = deployType;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getDeployId() {
        return deployId;
    }

    public void setDeployId(long deployId) {
        this.deployId = deployId;
    }

    public int getDeployType() {
        return deployType;
    }

    public void setDeployType(int deployType) {
        this.deployType = deployType;
    }

    public String getBambooDeployVersion() {
        return bambooDeployVersion;
    }

    public void setBambooDeployVersion(String bambooDeployVersion) {
        this.bambooDeployVersion = bambooDeployVersion;
    }

    public int getBambooBuildNumber() {
        return bambooBuildNumber;
    }

    public void setBambooBuildNumber(int bambooBuildNumber) {
        this.bambooBuildNumber = bambooBuildNumber;
    }

    public String getBambooDeployProject() {
        return bambooDeployProject;
    }

    public void setBambooDeployProject(String bambooDeployProject) {
        this.bambooDeployProject = bambooDeployProject;
    }

    public boolean isBambooDeployRollback() {
        return bambooDeployRollback;
    }

    public void setBambooDeployRollback(boolean bambooDeployRollback) {
        this.bambooDeployRollback = bambooDeployRollback;
    }

    public String getBambooManualBuildTriggerReasonUserName() {
        return bambooManualBuildTriggerReasonUserName;
    }

    public void setBambooManualBuildTriggerReasonUserName(String bambooManualBuildTriggerReasonUserName) {
        this.bambooManualBuildTriggerReasonUserName = bambooManualBuildTriggerReasonUserName;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
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

    public UserVO getUserVO() {
        return userVO;
    }

    public void setUserVO(UserVO userVO) {
        this.userVO = userVO;
    }

    public enum DeployTypeEnum {
        //0 保留
        war(0, "war"),
        pkg(1, "pkg");
        private int code;
        private String desc;

        DeployTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getDeployTypeName(int code) {
            for (DeployTypeEnum deployTypeEnum : DeployTypeEnum.values()) {
                if (deployTypeEnum.getCode() == code) {
                    return deployTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }


    @Override
    public String toString() {
        return "CiDeployStatisticsDO{" +
                "projectName='" + projectName + '\'' +
                ", groupName='" + groupName + '\'' +
                ", env='" + env + '\'' +
                ", status=" + status +
                ", deployId=" + deployId +
                ", deployType=" + deployType +
                ", bambooDeployVersion='" + bambooDeployVersion + '\'' +
                ", bambooBuildNumber=" + bambooBuildNumber +
                ", bambooDeployProject='" + bambooDeployProject + '\'' +
                ", bambooDeployRollback=" + bambooDeployRollback +
                ", bambooManualBuildTriggerReasonUserName='" + bambooManualBuildTriggerReasonUserName + '\'' +
                ", branchName='" + branchName + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }

}
