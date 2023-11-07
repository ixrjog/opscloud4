package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.param.apollo.ApolloParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketParam;
import com.baiyi.opscloud.facade.apollo.ApolloFacade;
import com.baiyi.opscloud.facade.workorder.WorkOrderTicketFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author baiyi
 * @Date 2022/6/8 17:15
 * @Version 1.0
 */

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/api/out")
@Tag(name = "外部API")
@RequiredArgsConstructor
public class OutApiController {

    private final WorkOrderTicketFacade workOrderTicketFacade;

    private final ApolloFacade apolloFacade;

    @Operation(summary = "审批工单票据")
    @GetMapping(value = "/ticket/approve", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult approveTicket(@RequestParam int ticketId, String username, String approvalType, String token) {
        WorkOrderTicketParam.OutApproveTicket outApproveTicket = WorkOrderTicketParam.OutApproveTicket.builder()
                .ticketId(ticketId)
                .username(username)
                .approvalType(approvalType)
                .token(token)
                .build();
        return workOrderTicketFacade.approveTicket(outApproveTicket);
    }

    @Operation(summary = "Apollo配置发布拦截器")
    @PostMapping(value = "/apollo/release/intercept", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult interceptRelease(@RequestBody ApolloParam.ReleaseEvent releaseEvent) {
        return apolloFacade.interceptRelease(releaseEvent);
    }

}