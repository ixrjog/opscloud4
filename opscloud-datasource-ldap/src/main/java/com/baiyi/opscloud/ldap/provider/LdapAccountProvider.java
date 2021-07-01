package com.baiyi.opscloud.ldap.provider;

import com.baiyi.opscloud.common.datasource.LdapDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsLdapConfig;
import com.baiyi.opscloud.common.type.DsAssetTypeEnum;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.builder.AssetContainer;
import com.baiyi.opscloud.datasource.factory.AssetProviderFactory;
import com.baiyi.opscloud.datasource.model.DsInstanceContext;
import com.baiyi.opscloud.datasource.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.datasource.util.AssetUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.ldap.convert.LdapAssetConvert;
import com.baiyi.opscloud.ldap.entry.Group;
import com.baiyi.opscloud.ldap.entry.Person;
import com.baiyi.opscloud.ldap.repo.PersonRepo;
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
