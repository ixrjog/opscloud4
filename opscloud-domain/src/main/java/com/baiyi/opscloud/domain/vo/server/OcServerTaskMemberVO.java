package com.baiyi.opscloud.domain.vo.server;

import com.baiyi.opscloud.domain.vo.env.OcEnvVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/4/9 4:50 下午
 * @Version 1.0
 */
public class OcServerTaskMemberVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ServerTaskMember {

        private OcEnvVO.Env env;

        private Integer id;
        private Integer taskId;
        private String hostPattern;
        private Integer serverId;
        private String manageIp;
        private String comment;
        private Integer finalized;
        private Integer stopType;
        private Integer exitValue;
        private String taskStatus;
        private String taskResult;
        private Date createTime;
        private Date updateTime;
        private String outputMsg;
        private String errorMsg;

    }
}
