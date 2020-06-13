package com.baiyi.opscloud.domain.vo.jumpserver;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/3/16 10:45 上午
 * @Version 1.0
 */
public class JumpserverTerminalSessionVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class TerminalSession {
        private String user;
        private String asset;
        private String systemUser;
        private Boolean hasReplay;
        private Boolean hasCommand;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date dateStart;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date dateEnd;
        private String terminalId;
        private String remoteAddr;
        private String protocol;
        private String assetId;
        private String terminalName;
    }
}
