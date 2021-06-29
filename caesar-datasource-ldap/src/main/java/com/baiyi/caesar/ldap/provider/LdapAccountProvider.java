package com.baiyi.caesar.ldap.provider;

import com.baiyi.caesar.common.datasource.LdapDsInstanceConfig;
import com.baiyi.caesar.common.datasource.config.DsLdapConfig;
import com.baiyi.caesar.common.type.DsAssetTypeEnum;
import com.baiyi.caesar.common.type.DsTypeEnum;
import com.baiyi.caesar.datasource.builder.AssetContainer;
import com.baiyi.caesar.datasource.factory.AssetProviderFactory;
import com.baiyi.caesar.datasource.model.DsInstanceContext;
import com.baiyi.caesar.datasource.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.caesar.datasource.util.AssetUtil;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import com.baiyi.caesar.ldap.convert.LdapAssetConvert;
import com.baiyi.caesar.ldap.entry.Group;
import com.baiyi.caesar.ldap.entry.Person;
import com.baiyi.caesar.ldap.repo.PersonRepo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/19 3:58 下午
 * @Version 1.0
 */
@Component
public class LdapAccountProvider extends AbstractAssetRelationProvider<Person, Group> {

    @Resource
    private PersonRepo personRepo;

    @Resource
    private LdapAccountProvider ldapAccountProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.LDAP.name();
    }

    private DsLdapConfig.Ldap buildConfig(DatasourceConfig dsConfig) {
        return dsFactory.build(dsConfig, LdapDsInstanceConfig.class).getLdap();
    }

    @Override
    protected List<Person> listEntries(DsInstanceContext dsInstanceContext, Group target) {
        DsLdapConfig.Ldap ldap = buildConfig(dsInstanceContext.getDsConfig());
        return personRepo.queryGroupMember(ldap, target.getGroupName());
    }

    @Override
    protected List<Person> listEntries(DsInstanceContext dsInstanceContext) {
        return personRepo.getPersonList(buildConfig(dsInstanceContext.getDsConfig()));
    }

    @Override
//    @SingleTask(name = "PullLdapUser", lockTime = 300)
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.USER.getType();
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeEnum.GROUP.getType();
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getAssetKey2(), asset.getAssetKey2()))
            return false;
        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
            return false;
        return true;
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, Person entry) {
        return LdapAssetConvert.toAssetContainer(dsInstance, entry);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(ldapAccountProvider);
    }
}
