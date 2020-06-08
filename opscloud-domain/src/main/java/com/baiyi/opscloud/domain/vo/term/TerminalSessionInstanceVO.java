package com.baiyi.opscloud.domain.vo.term;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/5/25 4:49 下午
 * @Version 1.0
 */
public class TerminalSessionInstanceVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class TerminalSessionInstance {

        private AuditLog auditLog;

        private Integer id;
        private String sessionId;
        private String instanceId;
        private String duplicateInstanceId;
        private String systemUser;
        private String hostIp;
        private Boolean isClosed;
        private Long outputSize;
        private String outputFileSize; // 输入文件容量
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date openTime;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date closeTime;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AuditLog {

        private Boolean isEmpty;
        private String path;
        private String content;

    }
}
