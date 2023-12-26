package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.domain.base.SimpleBusiness;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.tag.BusinessTagService;
import com.baiyi.opscloud.service.tag.TagService;
import com.baiyi.opscloud.service.user.UserPermissionService;
import com.baiyi.opscloud.workorder.constants.OrderTicketPhaseCodeConstants;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
import com.baiyi.opscloud.workorder.processor.impl.base.BaseTicketProcessor;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/8/28 11:48
 * @Version 1.0
 */
@Component
public class ApplicationFinOpsTagTicketProcessor extends BaseTicketProcessor<Application> {

    @Resource
    private ApplicationService applicationService;

    @Resource
    private UserPermissionService userPermissionService;

    @Resource
    private TagService tagService;

    @Resource
    private BusinessTagService businessTagService;

    @Override
    protected void process(WorkOrderTicketEntry ticketEntry, Application entry) throws TicketProcessException {
        final String finOpsTag = ticketEntry.getRole();
        SimpleBusiness query = SimpleBusiness.builder()
                .businessType(ticketEntry.getBusinessType())
                .businessId(ticketEntry.getBusinessId())
                .build();

        // 清除所有FinOps tags
        for (BusinessTag e : businessTagService.queryByBusiness(query)) {
            Tag tag = tagService.getById(e.getTagId());
            if (tag.getTagKey().startsWith("$")) {
                if (tag.getTagKey().equals(finOpsTag)) {
                    // FinOps tag 已存在
                    return;
                } else {
                    businessTagService.deleteById(e.getId());
                }
            }
        }

        Tag tag = tagService.getByTagKey(finOpsTag);
        if (tag == null) {
            throw new TicketProcessException("FinOps tag does not exist: {}", finOpsTag);
        }
        // 附加FinOps tag
        BusinessTag businessTag = BusinessTag.builder()
                .businessType(ticketEntry.getBusinessType())
                .businessId(ticketEntry.getBusinessId())
                .tagId(tag.getId())
                .build();
        businessTagService.add(businessTag);
    }

    /**
     * 更新工单条目配置(附加FinOps Tag)
     *
     * @param ticketEntry
     */
    protected void handleUpdate(WorkOrderTicketEntryParam.TicketEntry ticketEntry) {
        WorkOrderTicket ticket = ticketService.getById(ticketEntry.getWorkOrderTicketId());
        if (!OrderTicketPhaseCodeConstants.NEW.name().equals(ticket.getTicketPhase())) {
            throw new TicketProcessException("工单进度不是新建，无法更新配置条目！");
        }
        final String finOpsTag = ticketEntry.getRole();
        checkFinOpsTag(finOpsTag);
        WorkOrderTicketEntry preTicketEntry = ticketEntryService.getById(ticketEntry.getId());
        preTicketEntry.setRole(finOpsTag);
        updateTicketEntry(preTicketEntry);
    }

    @Override
    public void handleVerify(WorkOrderTicketEntryParam.TicketEntry ticketEntry) throws TicketVerifyException {
        Application entry = this.toEntry(ticketEntry.getContent());
        if (StringUtils.isEmpty(entry.getApplicationKey())) {
            throw new TicketVerifyException("校验工单条目失败: 未指定应用Key！");
        }
        Application application = applicationService.getByKey(entry.getApplicationKey());
        if (application == null) {
            throw new TicketVerifyException("校验工单条目失败: 应用不存在！");
        }
        final String finOpsTag = ticketEntry.getRole();
        checkFinOpsTag(finOpsTag);
    }

    private void checkFinOpsTag(String finOpsTag) {
        if (StringUtils.isBlank(finOpsTag)) {
            throw new TicketProcessException("FinOps tag not specified");
        }
        // FinOps Tag 约定为$开头
        if (!finOpsTag.startsWith("$")) {
            throw new TicketProcessException("FinOps incorrect tag: {}", finOpsTag);
        }
        if (tagService.getByTagKey(finOpsTag) == null) {
            throw new TicketProcessException("FinOps tag does not exist: {}", finOpsTag);
        }
    }

    @Override
    public void update(WorkOrderTicketEntryParam.TicketEntry ticketEntry) {
        handleUpdate(ticketEntry);
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.APPLICATION_FINOPS_TAG.name();
    }

    @Override
    protected Class<Application> getEntryClassT() {
        return Application.class;
    }

}