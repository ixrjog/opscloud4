package com.baiyi.opscloud.domain.vo.workorder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2022/1/19 1:15 PM
 * @Version 1.0
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimpleApprover implements WorkOrderTicketVO.IApprover {

    private Integer ticketId;

    private Boolean isApprover;

}