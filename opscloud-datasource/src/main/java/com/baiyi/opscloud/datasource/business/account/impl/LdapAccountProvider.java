package com.baiyi.opscloud.datasource.business.account.impl;

import com.baiyi.opscloud.common.datasource.LdapConfig;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.datasource.business.account.convert.AccountConvert;
import com.baiyi.opscloud.datasource.business.account.impl.base.AbstractAccountProvider;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.UserGroup;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.datasource.ldap.repo.GroupRepo;
import com.baiyi.opscloud.datasource.ldap.repo.PersonRepo;
import com.baiyi.opscloud.service.user.UserGroupService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/11 2:09 下午
 * @Version 1.0
 */
@Component
public class LdapAccountProvider extends AbstractAccountProvider {

    @Resource
    private PersonRepo personRepo;

    @Resource
    private GroupRepo groupRepo;

    @Resource
    private UserGroupService userGroupService;

    protected static ThreadLocal<LdapConfig.Ldap> configContext = new ThreadLocal<>();

    @Override
    protected void initialConfig(DatasourceConfig dsConfig) {
        configContext.set(dsConfigHelper.build(dsConfig, LdapConfig.class).getLdap());
    }

    @Override
    protected void doCreate(User user) {
        if (!personRepo.checkPersonInLdap(configContext.get(), user.getUsername()))
            personRepo.create(configContext.get(), AccountConvert.toLdapPerson(user));
    }

    @Override
    protected void doUpdate(User user) {
        if (personRepo.checkPersonInLdap(configContext.get(), user.getUsername()))
            personRepo.update(configContext.get(), AccountConvert.toLdapPerson(user));
    }

    @Override
    protected void doDelete(User user) {
        if (personRepo.checkPersonInLdap(configContext.get(), user.getUsername()))
            personRepo.delete(configContext.get(), user.getUsername());
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
