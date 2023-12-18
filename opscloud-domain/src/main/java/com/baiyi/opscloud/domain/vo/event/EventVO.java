package com.baiyi.opscloud.domain.vo.event;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/10/9 2:32 下午
 * @Version 1.0
 */
public class EventVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @Schema
    public static class Event extends BaseVO implements Serializable {

        @Serial
        private static final long serialVersionUID = -1509640973227933L;

        private Integer id;

        @Schema(description = "数据源实例UUID")
        private String instanceUuid;

        @Schema(description = "事件ID")
        private String eventId;

        @Schema(description = "事件ID描述")
        private String eventIdDesc;

        @Schema(description = "严重性级别")
        private Integer priority;

        @Schema(description = "严重类型")
        private String severityType;

        @Schema(description = "最后更改其状态的时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date lastchangeTime;

        @Schema(description = "过期时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date expiredTime;

        @Schema(description = "事件信息")
        private String eventMessage;

    }

}