package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.datasource.facade.UserRamFacade;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.param.user.UserRamParam;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.processor.impl.base.AbstractAssetPermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 阿里云访问控制策略权限申请工单票据处理
 * @Author baiyi
 * @Date 2022/1/7 2:04 PM
 * @Version 1.0
 */
@Slf4j
@Component
public class RamPolicyTicketProcessor extends AbstractAssetPermission {

    @Resource
    private UserRamFacade userRamFacade;

    @Override
    protected void process(WorkOrderTicketEntry ticketEntry, DatasourceInstanceAsset entry) throws TicketProcessException {
        User applicantUser = queryApplicant(ticketEntry);
        UserRamParam.Policy policy = UserRamParam.Policy.builder()
                .policyName(entry.getAssetId())
                .policyType(entry.getAssetKey())
                .build();
        UserRamParam.GrantRamPolicy grantRamPolicy = UserRamParam.GrantRamPolicy.builder()
                .instanceUuid(ticketEntry.getInstanceUuid())
                .username(applicantUser.getUsername())
                .policy(policy)
                .build();
        try {
            userRamFacade.grantRamPolicy(grantRamPolicy);
        } catch (Exception e) {
            throw new TicketProcessException("工单授权策略失败: " + e.getMessage());
        }
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
