package com.baiyi.opscloud.domain.vo.task;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/9/18 3:46 下午
 * @Version 1.0
 */
public class ServerTaskMemberVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @ApiModel
    public static class Member extends BaseVO {

        private Integer id;

        private Integer serverTaskId;

        private Integer serverId;

        private String serverName;

        private Integer envType;

        private String manageIp;

        private Boolean finalized;

        private Integer stopType;

        private Integer exitValue;

        private String taskResult;

        private Date startTime;

        private Date endTime;

        private String outputMsg;

        private String errorMsg;
    }
}
