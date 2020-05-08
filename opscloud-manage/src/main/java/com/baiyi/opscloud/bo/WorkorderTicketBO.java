package com.baiyi.opscloud.bo;

import com.baiyi.opscloud.common.base.TicketPhase;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/4/28 11:06 上午
 * @Version 1.0
 */
@Data
@Builder
public class WorkorderTicketBO {

    private Integer id;
    private Integer workorderId;
    private Integer userId;
    private String username;
    private String userDetail;
    private String comment;
    @Builder.Default
    private String ticketPhase = TicketPhase.CREATED.getPhase();
    @Builder.Default
    private Integer ticketStatus = 0;
    private Integer flowId;
    private Date startTime;
    private Date endTime;
    private Date createTime;
    private Date updateTime;
}
