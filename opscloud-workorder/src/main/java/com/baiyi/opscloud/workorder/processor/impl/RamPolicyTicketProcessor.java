package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.datasource.facade.UserAmFacade;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.param.user.UserAmParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
import com.baiyi.opscloud.workorder.processor.impl.extended.AbstractDsAssetPermissionExtendedBaseTicketProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * 阿里云访问控制策略权限申请工单票据处理
 *
 * @Author baiyi
 * @Date 2022/1/7 2:04 PM
 * @Version 1.0
 */
@Slf4j
@Component
public class RamPolicyTicketProcessor extends AbstractDsAssetPermissionExtendedBaseTicketProcessor {

    @Resource
    private UserAmFacade userAmFacade;

    @Override
    protected void process(WorkOrderTicketEntry ticketEntry, DatasourceInstanceAsset entry) throws TicketProcessException {
        User createUser = queryCreateUser(ticketEntry);
        try {
            preProcess(ticketEntry, createUser);
        } catch (Exception e) {
            throw new TicketProcessException("工单创建RAM账户错误: {}", e.getMessage());
        }
        UserAmParam.Policy policy = UserAmParam.Policy.builder()
                .policyName(entry.getAssetId())
                .policyType(entry.getAssetKey())
                .build();
        UserAmParam.GrantPolicy grantPolicy = UserAmParam.GrantPolicy.builder()
                .instanceUuid(ticketEntry.getInstanceUuid())
                .username(createUser.getUsername())
                .policy(policy)
                .build();
        try {
            userAmFacade.grantPolicy(grantPolicy);
        } catch (Exception e) {
            throw new TicketProcessException("工单授权策略失败: {}", e.getMessage());
        }
    }

    /**
     * 创建RAM用户
     *
     * @param ticketEntry
     * @param user
     */
    private void preProcess(WorkOrderTicketEntry ticketEntry, User user) {
        UserAmParam.CreateUser createUser = UserAmParam.CreateUser.builder()
                .instanceUuid(ticketEntry.getInstanceUuid())
                .username(user.getUsername())
                .build();
        userAmFacade.createUser(createUser);
    }

    @Override
    public void handleVerify(WorkOrderTicketEntryParam.TicketEntry ticketEntry) throws TicketVerifyException {
        DatasourceInstanceAsset entry = this.toEntry(ticketEntry.getContent());
        DatasourceInstanceAsset asset = getAsset(entry);
        verifyEntry(asset);
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.RAM_POLICY.name();
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

}