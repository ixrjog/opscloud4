package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.NacosConfig;
import com.baiyi.opscloud.core.InstanceHelper;
import com.baiyi.opscloud.datasource.nacos.driver.NacosAuthDriver;
import com.baiyi.opscloud.datasource.nacos.entity.NacosUser;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
import com.baiyi.opscloud.workorder.processor.impl.extended.AbstractDsAssetExtendedBaseTicketProcessor;
import com.google.common.base.Joiner;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import jakarta.annotation.Resource;

import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/1/13 7:38 PM
 * @Version 1.0
 */
@Slf4j
@Component
public class NacosTicketProcessor extends AbstractDsAssetExtendedBaseTicketProcessor<DatasourceInstanceAsset, NacosConfig> {

    @Resource
    private NacosAuthDriver nacosAuthDrive;

    @Resource
    private InstanceHelper instanceHelper;

    private static final String RANDOM_PASSWORD = null;

    /**
     * 创建Nacos用户
     *
     * @param config
     * @param nacosUsername
     */
    private void createNacosUser(NacosConfig.Nacos config, String nacosUsername) {
        List<String> userNames = nacosAuthDrive.searchUsers(config, nacosUsername);
        if (!CollectionUtils.isEmpty(userNames)) {
            if (userNames.stream().anyMatch(n -> n.equals(nacosUsername))) {
                return;
            }
        }
        nacosAuthDrive.createUser(config, nacosUsername, RANDOM_PASSWORD);
    }

    @Override
    protected void processHandle(WorkOrderTicketEntry ticketEntry, DatasourceInstanceAsset entry) throws TicketProcessException {
        NacosConfig.Nacos config = getDsConfig(ticketEntry, NacosConfig.class).getNacos();

        final String prefix = Optional.ofNullable(config.getAccount())
                .map(NacosConfig.Account::getPrefix)
                .orElse("");
        WorkOrderTicket ticket = getTicketById(ticketEntry.getWorkOrderTicketId());
        String nacosUsername = Joiner.on("").skipNulls().join(prefix, ticket.getUsername());
        try {
            createNacosUser(config, nacosUsername);
        } catch (Exception e) {
            throw new TicketProcessException("Create Nacos user err: {}", e.getMessage());
        }
        try {
            NacosUser.AuthRoleResponse authRoleResponse = nacosAuthDrive.authRole(config, nacosUsername, ticketEntry.getName());
            if (authRoleResponse.getCode() != HttpStatus.SC_OK) {
                throw new TicketProcessException("工单配置Nacos失败: {}", authRoleResponse.getMessage());
            }
        } catch (FeignException e) {
            if (e.status() == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                log.info("Nacos授权角色接口500错误: 可能是重复申请导致的！");
            } else {
                throw new TicketProcessException("工单配置Nacos失败: {}", e.getMessage());
            }
        }
    }

    @Override
    protected void pullAsset(WorkOrderTicketEntry ticketEntry, DatasourceInstanceAsset entry) {
        DsAssetParam.PullAsset pullAsset = DsAssetParam.PullAsset.builder()
                .instanceId(instanceHelper.getInstanceByUuid(ticketEntry.getInstanceUuid()).getId())
                .assetType(getAssetType())
                .build();
        dsInstanceFacade.pullAsset(pullAsset);
    }

    @Override
    public void handleVerify(WorkOrderTicketEntryParam.TicketEntry ticketEntry) throws TicketVerifyException {
        DatasourceInstanceAsset entry = this.toEntry(ticketEntry.getContent());
        DatasourceInstanceAsset asset = getAsset(entry);
        verifyEntry(asset);
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.NACOS.name();
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.NACOS.name();
    }

    @Override
    protected Class<DatasourceInstanceAsset> getEntryClassT() {
        return DatasourceInstanceAsset.class;
    }

}