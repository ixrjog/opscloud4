package com.baiyi.opscloud.datasource.ldap.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.LdapConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.datasource.ldap.entity.LdapGroup;
import com.baiyi.opscloud.datasource.ldap.entity.LdapPerson;
import com.baiyi.opscloud.datasource.ldap.repo.GroupRepo;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_LDAP_GROUP;

/**
 * @Author baiyi
 * @Date 2021/6/19 6:09 下午
 * @Version 1.0
 */
@Component
public class LdapGroupProvider extends AbstractAssetRelationProvider<LdapGroup.Group, LdapPerson.Person> {

    @Resource
    private GroupRepo groupRepo;

    @Resource
    private LdapGroupProvider ldapGroupProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.LDAP.name();
    }

    private LdapConfig.Ldap buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, LdapConfig.class).getLdap();
    }

    @Override
    protected List<LdapGroup.Group> listEntities(DsInstanceContext dsInstanceContext, LdapPerson.Person target) {
        LdapConfig.Ldap ldap = buildConfig(dsInstanceContext.getDsConfig());
        return groupRepo.searchGroupByUsername(ldap, target.getUsername());
    }

    @Override
    protected List<LdapGroup.Group> listEntities(DsInstanceContext dsInstanceContext) {
        return groupRepo.getGroupList(buildConfig(dsInstanceContext.getDsConfig()));
    }

    @Override
    @SingleTask(name = PULL_LDAP_GROUP, lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.GROUP.name();
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeConstants.USER.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(ldapGroupProvider);
    }

}