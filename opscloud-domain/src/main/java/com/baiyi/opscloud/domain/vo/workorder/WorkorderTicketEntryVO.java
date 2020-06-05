package com.baiyi.opscloud.domain.vo.workorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/4/29 9:34 上午
 * @Version 1.0
 */
public class WorkorderTicketEntryVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Entry<T> {
        @ApiModelProperty(value = "条目对象")
        private T ticketEntry;

        private Integer id;
        private Integer workorderTicketId;
        private String name;
        private Integer businessId;
        private Integer entryStatus;
        private String comment;
        private String entryKey;
        private String entryResult;
        private Date createTime;
        private Date updateTime;
        private String entryDetail;
    }

}
