package com.baiyi.opscloud.domain.vo.terminal;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/7/22 4:09 下午
 * @Version 1.0
 */
public class TerminalSessionVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Session extends BaseVO implements TerminalSessionInstanceVO.ISessionInstances, UserVO.IUser, Serializable {

        @Serial
        private static final long serialVersionUID = 7671719435444459757L;
        private List<TerminalSessionInstanceVO.SessionInstance> sessionInstances;

        private UserVO.User user;

        private Integer id;
        private String sessionId;
        private Integer userId;
        private String username;
        private String remoteAddr;
        @Schema(description = "会话关闭")
        private Boolean sessionClosed;
        @Schema(description = "会话关闭事件")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date closeTime;
        private String serverHostname;
        private String serverAddr;
        @Schema(description = "会话类型")
        private String sessionType;

    }

}