package com.baiyi.opscloud.domain.vo.terminal;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/7/22 4:14 下午
 * @Version 1.0
 */
public class TerminalSessionInstanceVO {

    public interface ISessionInstances {
        void setSessionInstances(List<TerminalSessionInstanceVO.SessionInstance> sessionInstances);
        String getSessionId();
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class SessionInstance extends BaseVO {
        @Schema(description = "会话时长")
        private Integer sessionDuration;

        private Integer id;
        private String sessionId;
        private String instanceId;
        private String duplicateInstanceId;
        private String instanceSessionType;
        @Schema(description = "会话账户")
        private String loginUser;
        private String hostIp;
        @Schema(description = "会话日志大小")
        private Long outputSize;
        @Schema(description = "是否关闭")
        private Boolean instanceClosed;
        @Schema(description = "命令数量")
        private Integer commandSize;
        @Schema(description = "会话实例开启时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date openTime;
        @Schema(description = "会话实例关闭时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date closeTime;
    }

}