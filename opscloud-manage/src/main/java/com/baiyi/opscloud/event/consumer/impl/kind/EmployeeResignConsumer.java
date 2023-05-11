package com.baiyi.opscloud.event.consumer.impl.kind;

import com.baiyi.opscloud.common.annotation.SetSessionUser;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.base.SimpleBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessAssetRelation;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.facade.workorder.WorkOrderTicketFacade;
import com.baiyi.opscloud.service.business.BusinessAssetRelationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * 消费资产与业务对象绑定关系事件
 * 消费员工离职消息
 *
 * @Author baiyi
 * @Date 2022/3/4 11:24 AM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class EmployeeResignConsumer {

    private final DsInstanceAssetService assetService;

    private final BusinessAssetRelationService bizAssetRelationService;

    private final UserService userService;

    private final WorkOrderTicketFacade workOrderTicketFacade;

    @SetSessionUser
    public void consume(BusinessAssetRelation eventData) {
        User user = userService.getById(eventData.getBusinessId());
        if (!user.getIsActive()) {
            return;
        }
        // 判断资产对象是否还存在
        DatasourceInstanceAsset asset = assetService.getById(eventData.getDatasourceInstanceAssetId());
        if (asset != null) {
            // 非钉钉资产解除绑定: 非离职事件
            if (!asset.getAssetType().equals(DsAssetTypeConstants.DINGTALK_USER.name())) {
                return;
            }
        } else {
            SimpleBusiness simpleBiz = SimpleBusiness.builder()
                    .businessType(BusinessTypeEnum.USER.getType())
                    .businessId(eventData.getBusinessId())
                    .build();
            // 用户有钉钉资产绑定关系: 无法确认是否离职
            List<BusinessAssetRelation> relations = bizAssetRelationService.queryBusinessRelations(simpleBiz, DsAssetTypeConstants.DINGTALK_USER.name());
            if (!CollectionUtils.isEmpty(relations)) {
                return;
            }
        }
        generateTicket(user);
    }

    public void generateTicket(User user) {
        // 创建离职工单
        WorkOrderTicketParam.CreateTicket createTicket = WorkOrderTicketParam.CreateTicket.builder()
                .workOrderKey(WorkOrderKeyConstants.SYS_EMPLOYEE_RESIGN.name())
                .build();
        WorkOrderTicketVO.TicketView ticketView = workOrderTicketFacade.createTicket(createTicket);
        // 插入离职用户条目
        WorkOrderTicketEntryParam.EntryQuery entryQuery = WorkOrderTicketEntryParam.EntryQuery.builder()
                .queryName(user.getUsername())
                .workOrderTicketId(ticketView.getTicketId())
                .build();
        List<WorkOrderTicketVO.Entry> entries = workOrderTicketFacade.queryTicketEntry(entryQuery);
        // 找出匹配的用户
        Optional<WorkOrderTicketVO.Entry> optionalEntry = entries.stream().filter(e -> e.getEntryKey().equals(user.getUsername())).findFirst();
        if (optionalEntry.isEmpty()) {
            return;
        }
        WorkOrderTicketEntryParam.TicketEntry entry = BeanCopierUtil.copyProperties(optionalEntry.get(), WorkOrderTicketEntryParam.TicketEntry.class);
        workOrderTicketFacade.addTicketEntry(entry);
        // 提交工单
        WorkOrderTicketParam.SubmitTicket submitTicket = WorkOrderTicketParam.SubmitTicket.builder()
                .ticketId(ticketView.getTicketId())
                .workflowView(ticketView.getWorkflowView())
                .build();
        workOrderTicketFacade.submitTicket(submitTicket);
    }

}
