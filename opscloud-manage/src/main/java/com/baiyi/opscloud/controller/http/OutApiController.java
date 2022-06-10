package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketParam;
import com.baiyi.opscloud.facade.workorder.WorkOrderTicketFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author baiyi
 * @Date 2022/6/8 17:15
 * @Version 1.0
 */

@RestController
@RequestMapping("/api/out")
@Api(tags = "外部API")
@RequiredArgsConstructor
public class OutApiController {

    private final WorkOrderTicketFacade workOrderTicketFacade;

    @ApiOperation(value = "审批工单票据")
    @GetMapping(value = "/ticket/approve", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult approveTicket(@RequestParam Integer ticketId, String username, String approvalType, String token) {
        WorkOrderTicketParam.OutApproveTicket outApproveTicket = WorkOrderTicketParam.OutApproveTicket.builder()
                .ticketId(ticketId)
                .username(username)
                .approvalType(approvalType)
                .token(token)
                .build();
        return workOrderTicketFacade.approveTicket(outApproveTicket);
    }

}



