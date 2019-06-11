package com.sdg.cmdb.domain.ci;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.auth.UserDO;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Data
public class CiBuildDO implements Serializable {

    private static final long serialVersionUID = -2639240641048774074L;

    public CiBuildDO() {
    }

    public CiBuildDO(CiJobVO ciJobVO, UserDO userDO, String parameters, boolean deploy) {
        this.jobId = ciJobVO.getId();
        if(deploy){
            this.jobName = ciJobVO.getDeployJobName();
        }else{
            this.jobName = ciJobVO.getJobName();
            this.versionName = ciJobVO.getVersionName();
            this.versionDesc = ciJobVO.getVersionDesc();
        }
        if(userDO == null || StringUtils.isEmpty(userDO.getUsername())){
            this.displayName = "系统任务";
        }else{
            this.displayName = userDO.getUsername() + "<" + userDO.getDisplayName() + ">";
            this.mail = userDO.getMail();
            this.mobile = userDO.getMobile();
        }

        this.parameters = parameters;
    }

    // 构建编号为队列
    public static final int BUILD_NUMBER_QUEUE = -1;

    private long id;
    private long jobId;
    private String jobName;
    private String parameters;
    private String displayName;
    private String mail;
    private String mobile;
    private int buildNumber;
    private String buildPhase;
    private String buildStatus;
    /**
     * 本次build的版本名称
     */
    private String versionName;
    private String versionDesc;
    private String commit;
    private String gmtCreate;
    private String gmtModify;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
