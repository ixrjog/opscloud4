package com.baiyi.opscloud.domain.vo.terminal;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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
    @ApiModel
    public static class Session extends BaseVO implements TerminalSessionInstanceVO.ISessionInstances, Serializable {

        private List<TerminalSessionInstanceVO.SessionInstance> sessionInstances;

        private Integer id;
        private String sessionId;
        private Integer userId;
        private String username;
        private String remoteAddr;
        @ApiModelProperty(value = "会话关闭")
        private Boolean sessionClosed;
        @ApiModelProperty(value = "会话关闭事件")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date closeTime;
        private String serverHostname;
        private String serverAddr;
        @ApiModelProperty(value = "会话类型")
        private String sessionType;

    }
}
