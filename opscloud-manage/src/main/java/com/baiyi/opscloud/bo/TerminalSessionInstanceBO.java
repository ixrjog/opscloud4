package com.baiyi.opscloud.bo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/5/25 11:19 上午
 * @Version 1.0
 */
@Data
@Builder
public class TerminalSessionInstanceBO {

    private Integer id;
    private String sessionId;
    private String instanceId;
    private String duplicateInstanceId;
    private String systemUser;
    private String hostIp;
    @Builder.Default
    private Long outputSize = 0L;
    @Builder.Default
    private Boolean isClosed = false;
    @Builder.Default
    private Date openTime = new Date();
    private Date closeTime;

}
