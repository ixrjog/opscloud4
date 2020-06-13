package com.baiyi.opscloud.bo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/5/24 12:40 下午
 * @Version 1.0
 */
@Data
@Builder
public class TerminalSessionBO {

    private Integer id;
    private String sessionId;
    private Integer userId;
    private String username;
    private String remoteAddr;
    @Builder.Default
    private Boolean isClosed = false;
    private Date closeTime;
    private String termHostname;
    private String termAddr;

}
