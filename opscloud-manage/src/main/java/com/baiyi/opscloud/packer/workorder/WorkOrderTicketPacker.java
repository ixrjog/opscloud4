package com.baiyi.opscloud.packer.workorder;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/1/11 3:14 PM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class WorkOrderTicketPacker {

    private final UserService userService;

    private final WorkOrderTicketEntryPacker ticketEntryPacker;

    private final WorkOrderPacker workOrderPacker;

    /**
     * 转换工单至视图
     *
     * @param workOrderTicket
     * @return
     */
    public WorkOrderTicketVO.TicketView toTicketView(WorkOrderTicket workOrderTicket) {
        WorkOrderTicketVO.Ticket ticket = BeanCopierUtil.copyProperties(workOrderTicket, WorkOrderTicketVO.Ticket.class);
        User user = userService.getByUsername(workOrderTicket.getUsername());
        WorkOrderTicketVO.TicketView ticketView = WorkOrderTicketVO.TicketView.builder()
                .createUser(toCreateUser(workOrderTicket))
                .workOrderTicket(ticket)
                .build();
        workOrderPacker.wrap(ticketView);
        ticketEntryPacker.wrap(ticketView);
        return ticketView;
    }

    private UserVO.User toCreateUser(WorkOrderTicket workOrderTicket) {
        User user = userService.getByUsername(workOrderTicket.getUsername());
        return BeanCopierUtil.copyProperties(user, UserVO.User.class);
    }

}
