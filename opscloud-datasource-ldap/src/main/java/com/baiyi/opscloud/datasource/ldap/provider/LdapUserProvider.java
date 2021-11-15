package com.baiyi.opscloud.datasource.ldap.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.LdapDsInstanceConfig;
import com.baiyi.opscloud.common.constant.enums.DsTypeEnum;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.datasource.ldap.entry.Group;
import com.baiyi.opscloud.datasource.ldap.entry.Person;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetRelationProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.datasource.ldap.convert.LdapAssetConvert;
import com.baiyi.opscloud.datasource.ldap.repo.PersonRepo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/19 3:58 下午
 * @Version 1.0
 */
@Component
public class LdapUserProvider extends AbstractAssetRelationProvider<Person, Group>  {

    @Resource
    private PersonRepo personRepo;

    @Resource
    private LdapUserProvider ldapUserProvider;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.LDAP.name();
    }

    private LdapDsInstanceConfig.Ldap buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, LdapDsInstanceConfig.class).getLdap();
    }

    @Override
    protected List<Person> listEntries(DsInstanceContext dsInstanceContext, Group target) {
        LdapDsInstanceConfig.Ldap ldap = buildConfig(dsInstanceContext.getDsConfig());
        return personRepo.queryGroupMember(ldap, target.getGroupName());
    }

    @Override
    protected List<Person> listEntries(DsInstanceContext dsInstanceContext) {
        return personRepo.getPersonList(buildConfig(dsInstanceContext.getDsConfig()));
    }

    @Override
    @SingleTask(name = "pull_ldap_user", lockTime = "5m")
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
        AssetProviderFactory.register(ldapUserProvider);
    }
}
