package com.baiyi.opscloud.domain.vo.task;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/9/18 3:46 下午
 * @Version 1.0
 */
public class ServerTaskMemberVO {

    public interface IServerTaskMembers {
        Integer getServerTaskId();
        void setServerTaskMembers(List<Member> serverTaskMembers);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Member extends BaseVO implements EnvVO.IEnv {

        private EnvVO.Env env;

        private Integer id;

        private Integer serverTaskId;

        private Integer serverId;

        private String serverName;

        private Integer envType;

        private String manageIp;

        private Boolean finalized;

        private String taskStatus;

        private Integer stopType;

        private Integer exitValue;

        private String taskResult;

        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date startTime;

        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date endTime;

        private String outputMsg;

        private String errorMsg;

    }

}