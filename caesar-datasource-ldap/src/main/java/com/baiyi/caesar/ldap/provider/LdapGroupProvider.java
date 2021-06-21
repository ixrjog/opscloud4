package com.baiyi.caesar.ldap.provider;

import com.baiyi.caesar.common.annotation.SingleTask;
import com.baiyi.caesar.common.datasource.LdapDsInstanceConfig;
import com.baiyi.caesar.common.datasource.config.LdapDsConfig;
import com.baiyi.caesar.common.type.DsAssetTypeEnum;
import com.baiyi.caesar.common.type.DsTypeEnum;
import com.baiyi.caesar.datasource.asset.AbstractAssetRelationProvider;
import com.baiyi.caesar.datasource.builder.AssetContainer;
import com.baiyi.caesar.datasource.factory.AssetProviderFactory;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import com.baiyi.caesar.ldap.convert.AccountAssetConvert;
import com.baiyi.caesar.ldap.entry.Group;
import com.baiyi.caesar.ldap.entry.Person;
import com.baiyi.caesar.ldap.repo.GroupRepo;
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

    private LdapDsConfig.Ldap buildConfig(DatasourceConfig dsConfig) {
        return dsFactory.build(dsConfig, LdapDsInstanceConfig.class).getLdap();
    }


    @Override
    protected List<Group> listEntries(DatasourceConfig dsConfig, Person target) {
        LdapDsConfig.Ldap ldap = buildConfig(dsConfig);
        return groupRepo.searchGroupByUsername(ldap, target.getUsername());
    }


    @Override
    protected List<Group> listEntries(DatasourceConfig dsConfig) {
        return groupRepo.getGroupList(buildConfig(dsConfig));
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
        return AccountAssetConvert.toAssetContainer(dsInstance, entry);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(ldapGroupProvider);
    }
}

