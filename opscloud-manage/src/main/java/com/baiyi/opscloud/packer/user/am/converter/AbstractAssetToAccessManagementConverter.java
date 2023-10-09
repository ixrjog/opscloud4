package com.baiyi.opscloud.packer.user.am.converter;

import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.user.AccessManagementVO;
import com.baiyi.opscloud.packer.user.am.AccessManagementPacker;
import com.baiyi.opscloud.packer.user.am.IAssetToAccessManagementConverter;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import org.springframework.beans.factory.InitializingBean;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/2/8 1:42 PM
 * @Version 1.0
 */
public abstract class AbstractAssetToAccessManagementConverter implements IAssetToAccessManagementConverter, InitializingBean {

    @Resource
    protected DsInstanceService dsInstanceService;

    @Resource
    protected DsConfigService dsConfigService;

    @Resource
    protected DsConfigManager dsConfigManager;

    @Override
    public AccessManagementVO.XAccessManagement toAM(DsAssetVO.Asset asset) {
        DatasourceInstance instance = dsInstanceService.getByUuid(asset.getInstanceUuid());
        DatasourceConfig datasourceConfig = dsConfigService.getById(instance.getConfigId());
        List<DsAssetVO.Asset> policies = toPolicies(asset);
        AccessManagementVO.XAccessManagement xam = AccessManagementVO.XAccessManagement.builder()
                .instanceUuid(instance.getUuid())
                .instanceName(instance.getInstanceName())
                .username(asset.getAssetKey())
                .name(asset.getName())
                .accessKeys(toAccessKeys(asset))
                .policies(toPolicies(asset))
                .build();
        wrap(xam,datasourceConfig);
        return xam;
    }

    abstract protected void wrap(AccessManagementVO.XAccessManagement xam, DatasourceConfig datasourceConfig);

    abstract protected List<DsAssetVO.Asset> toAccessKeys(DsAssetVO.Asset asset);

    abstract protected List<DsAssetVO.Asset> toPolicies(DsAssetVO.Asset asset);

    @Override
    public void afterPropertiesSet() throws Exception {
        AccessManagementPacker.CONTEXT.put(getAMType(), this::toAM);
    }

}
