package com.baiyi.opscloud.workorder.helper.strategy.base;

import com.baiyi.opscloud.datasource.manager.base.NoticeManager;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.notice.INoticeMessage;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.service.workorder.WorkOrderService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketEntryService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketNodeService;
import com.baiyi.opscloud.workorder.helper.TicketNoticeHelper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/27 4:47 PM
 * @Version 1.0
 */
public abstract class AbstractSendNotice implements ISendNotice, InitializingBean {

    @Resource
    protected WorkOrderTicketEntryService ticketEntryService;

    @Resource
    protected WorkOrderTicketNodeService ticketNodeService;

    @Resource
    protected WorkOrderService workOrderService;

    @Resource
    protected UserService userService;

    @Resource
    protected NoticeManager noticeManager;

    abstract protected INoticeMessage buildNoticeMessage(WorkOrderTicket ticket);

    protected void send(List<User> users, String msgKey, INoticeMessage noticeMessage) {
        if (CollectionUtils.isEmpty(users)) return;
        users.forEach(user -> noticeManager.sendMessage(user, msgKey, noticeMessage));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        getPhases().forEach(p -> TicketNoticeHelper.context.put(p, this::send));
    }

}
