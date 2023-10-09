package com.baiyi.opscloud.datasource.business.account.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.iam.driver.AmazonIdentityManagementUserDriver;
import com.baiyi.opscloud.datasource.aws.iam.entity.IamUser;
import com.baiyi.opscloud.datasource.business.account.impl.base.AbstractAccountHandler;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/3/31 09:36
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class IamAccountHandler extends AbstractAccountHandler {

    private final AmazonIdentityManagementUserDriver amazonIMUserDriver;

    protected static ThreadLocal<AwsConfig.Aws> configContext = new ThreadLocal<>();

    @Override
    protected void initialConfig(DatasourceConfig dsConfig) {
        configContext.set(dsConfigManager.build(dsConfig, AwsConfig.class).getAws());
    }

    @Override
    protected void doCreate(User user) {
        // Not Supported
    }

    @Override
    protected void doUpdate(User user) {
        // TODO 从资产表查询用户，对比用户信息后再通过 API 更新 IAM 信息
    }

    @Override
    protected void doDelete(User user) {
        AwsConfig.Aws aws = configContext.get();
        try {
            IamUser.User iamUser = amazonIMUserDriver.getUser(aws, user.getUsername());
            if (iamUser != null) {
                amazonIMUserDriver.deleteLoginProfile(aws, user.getUsername());
            }
        } catch (Exception e) {
            log.error("删除IAM用户错误: username={}, {}", user.getUsername(), e.getMessage());
        }
    }

    @Override
    public void doGrant(User user, BaseBusiness.IBusiness businessResource) {
        // Not Supported
    }

    @Override
    public void doRevoke(User user, BaseBusiness.IBusiness businessResource) {
        // Not Supported
    }

    @Override
    protected int getBusinessResourceType() {
        return BusinessTypeEnum.COMMON.getType();
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.AWS.getName();
    }

}
