package com.baiyi.opscloud.domain.vo.term;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/25 4:17 下午
 * @Version 1.0
 */
public class TerminalSessionVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class TerminalSession {

        private List<TerminalSessionInstanceVO.TerminalSessionInstance> sessionInstances;

        private Integer id;
        private String sessionId;
        private Integer userId;
        private String username;
        private String remoteAddr;
        private Boolean isClosed;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date closeTime;
        private String termHostname;
        private String termAddr;

    }
}
