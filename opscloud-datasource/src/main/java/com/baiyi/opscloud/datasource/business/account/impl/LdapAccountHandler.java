package com.baiyi.opscloud.datasource.business.account.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.LdapConfig;
import com.baiyi.opscloud.datasource.business.account.converter.AccountConverter;
import com.baiyi.opscloud.datasource.business.account.impl.base.AbstractAccountHandler;
import com.baiyi.opscloud.datasource.ldap.repo.GroupRepo;
import com.baiyi.opscloud.datasource.ldap.repo.PersonRepo;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.UserGroup;
import com.baiyi.opscloud.service.user.UserGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/8/11 2:09 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class LdapAccountHandler extends AbstractAccountHandler {

    private final PersonRepo personRepo;

    private final GroupRepo groupRepo;

    private final UserGroupService userGroupService;

    protected static ThreadLocal<LdapConfig.Ldap> configContext = new ThreadLocal<>();

    @Override
    protected void initialConfig(DatasourceConfig dsConfig) {
        configContext.set(dsConfigManager.build(dsConfig, LdapConfig.class).getLdap());
    }

    @Override
    protected void doCreate(User user) {
        if (!personRepo.checkPersonInLdap(configContext.get(), user.getUsername())) {
            personRepo.create(configContext.get(), AccountConverter.toLdapPerson(user));
        }
    }

    @Override
    protected void doUpdate(User user) {
        if (personRepo.checkPersonInLdap(configContext.get(), user.getUsername())) {
            personRepo.update(configContext.get(), AccountConverter.toLdapPerson(user));
        }
    }

    @Override
    protected void doDelete(User user) {
        if (personRepo.checkPersonInLdap(configContext.get(), user.getUsername())) {
            // 清理用户组残留用户
            List<String> userGroups = personRepo.searchUserGroupByUsername(configContext.get(), user.getUsername());
            if (!CollectionUtils.isEmpty(userGroups)) {
                userGroups.forEach(group -> groupRepo.removeGroupMember(configContext.get(), group, user.getUsername()));
            }
            personRepo.delete(configContext.get(), user.getUsername());
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
