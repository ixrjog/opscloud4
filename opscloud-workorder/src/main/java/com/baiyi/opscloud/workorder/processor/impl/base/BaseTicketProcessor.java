package com.baiyi.opscloud.workorder.processor.impl.base;

import com.baiyi.opscloud.domain.base.IAllowOrder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketEntryService;
import com.baiyi.opscloud.service.workorder.WorkOrderTicketService;
import com.baiyi.opscloud.workorder.constants.ProcessStatusConstants;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
import com.baiyi.opscloud.workorder.processor.ITicketProcessor;
import com.baiyi.opscloud.workorder.processor.factory.WorkOrderTicketProcessorFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import jakarta.annotation.Resource;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2022/1/6 6:58 PM
 * @Version 1.0
 */
@Slf4j
public abstract class BaseTicketProcessor<T> implements ITicketProcessor<T>, InitializingBean {

    @Resource
    protected WorkOrderTicketEntryService ticketEntryService;

    @Resource
    protected WorkOrderTicketService ticketService;

    @Resource
    private UserService userService;

    /**
     * 处理工单条目
     *
     * @param ticketEntry
     * @param entry
     */
    abstract protected void process(WorkOrderTicketEntry ticketEntry, T entry) throws TicketProcessException;

    /**
     * 取条目ClassT
     *
     * @return
     */
    abstract protected Class<T> getEntryClassT();

    @Override
    public T toEntry(String entryContent) throws JsonSyntaxException {
        final Gson builder = new GsonBuilder()
                .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (jsonElement, type, context)
                        -> new Date(jsonElement.getAsJsonPrimitive().getAsLong())).create();
        return builder.fromJson(entryContent, getEntryClassT());
    }

    @Override
    public void verify(WorkOrderTicketEntryParam.TicketEntry ticketEntry) throws TicketVerifyException {
        if (ticketEntryService.countByTicketUniqueKey(ticketEntry) != 0) {
            throw new TicketVerifyException("校验工单条目失败: 重复申请！");
        }
        handleVerify(ticketEntry);
    }

    /**
     * 验证
     * @param ticketEntry
     * @throws TicketVerifyException
     */
    abstract protected void handleVerify(WorkOrderTicketEntryParam.TicketEntry ticketEntry) throws TicketVerifyException;

    /**
     * 查询创建人
     *
     * @param ticketEntry
     * @return
     */
    protected User queryCreateUser(WorkOrderTicketEntry ticketEntry) {
        WorkOrderTicket workOrderTicket = ticketService.getById(ticketEntry.getWorkOrderTicketId());
        return userService.getByUsername(workOrderTicket.getUsername());
    }

    @Override
    public void process(WorkOrderTicketEntry ticketEntry) {
        try {
            this.process(ticketEntry, toEntry(ticketEntry.getContent()));
            ticketEntry.setEntryStatus(ProcessStatusConstants.SUCCESSFUL.getStatus());
        } catch (TicketProcessException e) {
            ticketEntry.setResult(e.getMessage());
            ticketEntry.setEntryStatus(ProcessStatusConstants.FAILED.getStatus());
        }
        updateTicketEntry(ticketEntry);
    }

    protected void updateTicketEntry(WorkOrderTicketEntry ticketEntry) {
        ticketEntryService.update(ticketEntry);
    }

    protected void verifyEntry(T entry) throws TicketVerifyException {
        if (entry instanceof IAllowOrder) {
            verifyByAllowOrder((IAllowOrder) entry);
        }
        if (entry instanceof DatasourceInstanceAsset) {
            verifyByAsset((DatasourceInstanceAsset) entry);
        }
    }

    private void verifyByAllowOrder(IAllowOrder allowOrder) throws TicketVerifyException {
        if (allowOrder.getAllowOrder() == null || !allowOrder.getAllowOrder()) {
            throw new TicketVerifyException("校验工单条目失败: 此条目不允许工单申请！");
        }
    }

    private void verifyByAsset(DatasourceInstanceAsset asset) throws TicketVerifyException {
        if (asset.getIsActive() == null || !asset.getIsActive()) {
            throw new TicketVerifyException("校验工单条目失败: 此授权资产无效！");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        WorkOrderTicketProcessorFactory.register(this);
    }

}