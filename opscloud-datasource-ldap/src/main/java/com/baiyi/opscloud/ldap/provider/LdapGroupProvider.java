package com.baiyi.opscloud.ldap.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.LdapDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsLdapConfig;
import com.baiyi.opscloud.common.type.DsAssetTypeEnum;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.model.DsInstanceContext;
import com.baiyi.opscloud.datasource.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.datasource.builder.AssetContainer;
import com.baiyi.opscloud.datasource.factory.AssetProviderFactory;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.ldap.convert.LdapAssetConvert;
import com.baiyi.opscloud.ldap.entry.Group;
import com.baiyi.opscloud.ldap.entry.Person;
import com.baiyi.opscloud.ldap.repo.GroupRepo;
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

    private DsLdapConfig.Ldap buildConfig(DatasourceConfig dsConfig) {
        return dsFactory.build(dsConfig, LdapDsInstanceConfig.class).getLdap();
    }


    @Override
    protected List<Group> listEntries(DsInstanceContext dsInstanceContext, Person target) {
        DsLdapConfig.Ldap ldap = buildConfig(dsInstanceContext.getDsConfig());
        return groupRepo.searchGroupByUsername(ldap, target.getUsername());
    }


    @Override
    protected List<Group> listEntries(DsInstanceContext dsInstanceContext) {
        return groupRepo.getGroupList(buildConfig(dsInstanceContext.getDsConfig()));
    }

    @Override
    @SingleTask(name = "PullLdapGroup", lockTime = 300)
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

