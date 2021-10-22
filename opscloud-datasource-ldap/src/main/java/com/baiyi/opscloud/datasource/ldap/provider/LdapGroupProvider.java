package com.baiyi.opscloud.datasource.ldap.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.LdapDsInstanceConfig;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.datasource.ldap.entry.Group;
import com.baiyi.opscloud.datasource.ldap.entry.Person;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.datasource.ldap.convert.LdapAssetConvert;
import com.baiyi.opscloud.datasource.ldap.repo.GroupRepo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/19 6:09 下午
 * @Version 1.0
 */
@Component
public class LdapGroupProvider extends AbstractAssetRelationProvider<Group, Person> {

    @Resource
    private GroupRepo groupRepo;

    @Resource
    private LdapGroupProvider ldapGroupProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.LDAP.name();
    }

    private LdapDsInstanceConfig.Ldap buildConfig(DatasourceConfig dsConfig) {
        return dsConfigFactory.build(dsConfig, LdapDsInstanceConfig.class).getLdap();
    }


    @Override
    protected List<Group> listEntries(DsInstanceContext dsInstanceContext, Person target) {
        LdapDsInstanceConfig.Ldap ldap = buildConfig(dsInstanceContext.getDsConfig());
        return groupRepo.searchGroupByUsername(ldap, target.getUsername());
    }


    @Override
    protected List<Group> listEntries(DsInstanceContext dsInstanceContext) {
        return groupRepo.getGroupList(buildConfig(dsInstanceContext.getDsConfig()));
    }

    @Override
    @SingleTask(name = "pull_ldap_group", lockTime = "5m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.GROUP.getType();
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeEnum.USER.getType();
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        return true;
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, Group entry) {
        return LdapAssetConvert.toAssetContainer(dsInstance, entry);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(ldapGroupProvider);
    }
}

