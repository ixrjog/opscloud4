package com.baiyi.opscloud.datasource.business.account.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.LdapConfig;
import com.baiyi.opscloud.datasource.business.account.impl.base.AbstractAccountGroupHandler;
import com.baiyi.opscloud.datasource.ldap.repo.GroupRepo;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.UserGroup;
import com.baiyi.opscloud.service.user.UserGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/9/14 5:45 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class LdapAccountGroupHandler extends AbstractAccountGroupHandler {

    private final GroupRepo groupRepo;

    private final UserGroupService userGroupService;

    protected static ThreadLocal<LdapConfig.Ldap> configContext = new ThreadLocal<>();

    @Override
    protected void initialConfig(DatasourceConfig dsConfig) {
        configContext.set(dsConfigManager.build(dsConfig, LdapConfig.class).getLdap());
    }

    @Override
    protected void doCreate(UserGroup userGroup) {
        try {
            groupRepo.create(configContext.get(), userGroup.getName());
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void doUpdate(UserGroup userGroup) {
    }

    @Override
    protected void doDelete(UserGroup userGroup) {
        try {
            groupRepo.delete(configContext.get(), userGroup.getName());
        } catch (Exception ignored) {
        }
    }

    @Override
    public void doGrant(User user, BaseBusiness.IBusiness businessResource) {
        groupRepo.addGroupMember(configContext.get(), getBusinessResource(businessResource.getBusinessId()).getName(), user.getUsername());
    }

    @Override
    public void doRevoke(User user, BaseBusiness.IBusiness businessResource) {
        groupRepo.removeGroupMember(configContext.get(), getBusinessResource(businessResource.getBusinessId()).getName(), user.getUsername());
    }

    private UserGroup getBusinessResource(int businessId) {
        return userGroupService.getById(businessId);
    }

    @Override
    public int getBusinessResourceType() {
        return BusinessTypeEnum.USERGROUP.getType();
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.LDAP.getName();
    }

}
