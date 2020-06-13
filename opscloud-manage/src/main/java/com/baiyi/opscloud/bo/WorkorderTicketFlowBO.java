package com.baiyi.opscloud.bo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/5/6 5:22 下午
 * @Version 1.0
 */
@Data
@Builder
public class WorkorderTicketFlowBO {

    private Integer id;
    private Integer ticketId;
    private Integer userId;
    private String username;
    private String flowName;
    @Builder.Default
    private Integer approvalType = -1;
    @Builder.Default
    private Integer flowParentId = 0; // 父流程
    private Integer approvalGroupId;
    @Builder.Default
    private Integer approvalStatus = -1;
    private String comment;

}
