package com.baiyi.opscloud.domain.vo.event;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
    @ApiModel
    public static class Event extends BaseVO implements Serializable {

        private static final long serialVersionUID = -1509640973227933L;

        private Integer id;

        @ApiModelProperty(value = "数据源实例uuid")
        private String instanceUuid;

        @ApiModelProperty(value = "事件id")
        private String eventId;

        @ApiModelProperty(value = "事件id描述")
        private String eventIdDesc;

        @ApiModelProperty(value = "严重性级别")
        private Integer priority;

        @ApiModelProperty(value = "严重类型")
        private String severityType;

        @ApiModelProperty(value = "最后更改其状态的时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date lastchangeTime;

        @ApiModelProperty(value = "过期时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date expiredTime;

        @ApiModelProperty(value = "事件信息")
        private String eventMessage;

    }

}
