package com.baiyi.opscloud.factory.ticket.impl;

import com.baiyi.opscloud.builder.WorkorderTicketBuilder;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.SessionUtils;
import com.baiyi.opscloud.decorator.WorkorderTicketDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorder;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicket;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicketEntry;
import com.baiyi.opscloud.domain.vo.workorder.OcWorkorderTicketEntryVO;
import com.baiyi.opscloud.domain.vo.workorder.OcWorkorderTicketVO;
import com.baiyi.opscloud.factory.ticket.ITicketHandler;
import com.baiyi.opscloud.factory.ticket.WorkorderTicketFactory;
import com.baiyi.opscloud.factory.ticket.entry.ITicketEntry;
import com.baiyi.opscloud.service.ticket.OcWorkorderTicketEntryService;
import com.baiyi.opscloud.service.ticket.OcWorkorderTicketService;
import com.baiyi.opscloud.service.workorder.OcWorkorderService;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/27 2:59 下午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseTicketHandler<T> implements ITicketHandler, InitializingBean {

    @Resource
    protected OcWorkorderTicketEntryService ocWorkorderTicketEntryService;

    @Resource
    private OcWorkorderTicketService ocWorkorderTicketService;

    @Resource
    private OcWorkorderService ocWorkorderService;

    @Resource
    private WorkorderTicketDecorator workorderTicketDecorator;

    public static final int ENTRY_STATUS_SUCCESS = 1;
    public static final int ENTRY_STATUS_ERROR = 2;

    @Override
    public void executorTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry) {
        // 获得条目详情
        try {
            T entry = getTicketEntry(ocWorkorderTicketEntry);
            executorTicketEntry(ocWorkorderTicketEntry, entry);
        } catch (JsonSyntaxException e) {
            ocWorkorderTicketEntry.setEntryStatus(2);
            ocWorkorderTicketEntry.setEntryResult("工单票据对象转换错误！");
        }
    }

    @Override
    public OcWorkorderTicketVO.Ticket createTicket(OcUser ocUser) {
        OcWorkorder ocWorkorder = acqOcWorkorder();
        ocUser.setPassword("");
        OcWorkorderTicket ocWorkorderTicket = WorkorderTicketBuilder.build(ocUser, ocWorkorder);

        OcWorkorderTicket pre = queryOcWorkorderTicketByParam(ocWorkorderTicket);
        if (pre != null)
            return getTicket(pre);

        ocWorkorderTicketService.addOcWorkorderTicket(ocWorkorderTicket);
        createTicketEntry(ocWorkorderTicket, ocUser);
        return getTicket(ocWorkorderTicket);
    }

    private OcWorkorderTicketVO.Ticket getTicket(OcWorkorderTicket ocWorkorderTicket) {
        OcWorkorderTicketVO.Ticket ticket = BeanCopierUtils.copyProperties(ocWorkorderTicket, OcWorkorderTicketVO.Ticket.class);
        return workorderTicketDecorator.decorator(ticket);
    }

    private OcWorkorderTicket queryOcWorkorderTicketByParam(OcWorkorderTicket queryParam) {
        List<OcWorkorderTicket> list = ocWorkorderTicketService.queryOcWorkorderTicketByParam(queryParam);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }

    @Override
    public OcWorkorderTicketEntryVO.Entry convertTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry) {
        OcWorkorderTicketEntryVO.Entry entry = BeanCopierUtils.copyProperties(ocWorkorderTicketEntry, OcWorkorderTicketEntryVO.Entry.class);
        entry.setTicketEntry(getTicketEntry(ocWorkorderTicketEntry));
        return entry;
    }


//    @Override
//    public BusinessWrapper<Boolean> addTicketEntry(OcUser ocUser, WorkorderTicketEntryParam.TicketEntry ticketEntry) {
//        OcWorkorderTicket ocWorkorderTicket = ocWorkorderTicketService.queryOcWorkorderTicketById(ticketEntry.getTicketId());
//        if (!ocWorkorderTicket.getUsername().equals(SessionUtils.getUsername()))
//            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_FAILUER);
//        ITicketEntry iTicketEntry = acqITicketEntry(ticketEntry);
//        OcWorkorderTicketEntry ocWorkorderTicketEntry = WorkorderTicketEntryBuilder.build(ticketEntry, iTicketEntry.getName());
//
//        ocWorkorderTicketEntryService.addOcWorkorderTicketEntry(ocWorkorderTicketEntry);
//        return BusinessWrapper.SUCCESS;
//    }

    @Override
    public BusinessWrapper<Boolean> addTicketEntry(OcUser ocUser, OcWorkorderTicketEntryVO.Entry entry) {
        OcWorkorderTicket ocWorkorderTicket = ocWorkorderTicketService.queryOcWorkorderTicketById(entry.getWorkorderTicketId());
        if (!ocWorkorderTicket.getUsername().equals(SessionUtils.getUsername()))
            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_FAILUER);
        //ITicketEntry iTicketEntry = acqITicketEntry(entry.getTicketEntry());
        OcWorkorderTicketEntry ocWorkorderTicketEntry = BeanCopierUtils.copyProperties(entry, OcWorkorderTicketEntry.class);
        ocWorkorderTicketEntryService.addOcWorkorderTicketEntry(ocWorkorderTicketEntry);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateTicketEntry(OcUser ocUser, OcWorkorderTicketEntryVO.Entry entry) {
        OcWorkorderTicket ocWorkorderTicket = ocWorkorderTicketService.queryOcWorkorderTicketById(entry.getWorkorderTicketId());
        if (!ocWorkorderTicket.getUsername().equals(SessionUtils.getUsername()))
            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_FAILUER);
        return updateTicketEntry(entry);
    }

    protected abstract BusinessWrapper<Boolean> updateTicketEntry(OcWorkorderTicketEntryVO.Entry entry);


//    @Override
//    public BusinessWrapper<Boolean> updateTicketEntry(OcUser ocUser, WorkorderTicketEntryParam.TicketEntry ticketEntry) {
//        OcWorkorderTicket ocWorkorderTicket = ocWorkorderTicketService.queryOcWorkorderTicketById(ticketEntry.getTicketId());
//        if (!ocWorkorderTicket.getUsername().equals(SessionUtils.getUsername()))
//            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_FAILUER);
//
//        OcWorkorderTicketEntry pre = ocWorkorderTicketEntryService.queryOcWorkorderTicketEntryById(ticketEntry.getId());
//
//        ITicketEntry iTicketEntry = acqITicketEntry(ticketEntry);
//        OcWorkorderTicketEntry ocWorkorderTicketEntry = WorkorderTicketEntryBuilder.build(ticketEntry, iTicketEntry.getName());
//        ocWorkorderTicketEntry.setId(pre.getId());
//
//        ocWorkorderTicketEntryService.updateOcWorkorderTicketEntry(ocWorkorderTicketEntry);
//        return BusinessWrapper.SUCCESS;
//    }


    // protected abstract ITicketEntry acqITicketEntry(WorkorderTicketEntryParam.TicketEntry ticketEntry);

    protected abstract ITicketEntry acqITicketEntry(Object ticketEntry);


    /**
     * 创建票据条目(初始化数据，可重写)
     *
     * @param ocWorkorderTicket
     * @param ocUser
     */
    protected void createTicketEntry(OcWorkorderTicket ocWorkorderTicket, OcUser ocUser) {
    }


    protected abstract String acqWorkorderKey();

    private OcWorkorder acqOcWorkorder() {
        return ocWorkorderService.queryOcWorkorderByWorkorderKey(acqWorkorderKey());
    }


    /**
     * 执行器
     *
     * @param ocWorkorderTicketEntry
     * @param entry
     */
    protected abstract void executorTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry, T entry);

    protected abstract T getTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry) throws JsonSyntaxException;

    protected OcUser getUser(int ticketId) {
        OcWorkorderTicket ocWorkorderTicket = getOcWorkorderTicket(ticketId);
        OcUser ocUser = new OcUser();
        ocUser.setId(ocWorkorderTicket.getUserId());
        ocUser.setUsername(ocWorkorderTicket.getUsername());
        return ocUser;
    }

    private OcWorkorderTicket getOcWorkorderTicket(int ticketId) {
        return ocWorkorderTicketService.queryOcWorkorderTicketById(ticketId);
    }

    protected void saveTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry, BusinessWrapper<Boolean> executorWrapper) {
        if (executorWrapper.isSuccess()) {
            ocWorkorderTicketEntry.setEntryStatus(ENTRY_STATUS_SUCCESS);
        } else {
            ocWorkorderTicketEntry.setEntryStatus(ENTRY_STATUS_ERROR);
            ocWorkorderTicketEntry.setEntryResult(executorWrapper.getDesc());
        }
        ocWorkorderTicketEntryService.updateOcWorkorderTicketEntry(ocWorkorderTicketEntry);
    }

    /**
     * 注册
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        WorkorderTicketFactory.register(this);
    }


}
