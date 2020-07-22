package com.baiyi.opscloud.domain.vo.server;

import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/4/9 4:50 下午
 * @Version 1.0
 */
public class ServerTaskMemberVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ServerTaskMember {

        private EnvVO.Env env;
        /** 格式化执行结果 **/
        private AnsibleResult result;

        private String outputMsgLog;
        private String errorMsgLog;

        private Boolean showErrorLog;

        private Integer id;
        private Integer taskId;
        private String hostPattern;
        private Integer serverId;
        private Integer envType;
        private String manageIp;
        private String comment;
        private Integer finalized;
        private Integer stopType;
        private Integer exitValue;
        private String taskStatus;
        private String taskResult;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;
        private Date updateTime;
        private String outputMsg;
        private String errorMsg;

        // 是否成功
        private Boolean success;
        // 结果中是否隐藏显示
        private Boolean hide;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    @JsonIgnoreProperties
    public static class AnsibleResult {

        private Boolean changed;
        private Integer rc;
        private String stderr;

        //private String stderr_lines;

        private String stdout;
        //private String stdout_lines;

    }

}
