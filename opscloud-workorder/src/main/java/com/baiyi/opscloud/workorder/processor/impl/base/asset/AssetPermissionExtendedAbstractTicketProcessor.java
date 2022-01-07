package com.baiyi.opscloud.workorder.processor.impl.base.asset;

import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.domain.base.IInstanceType;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.workorder.processor.impl.base.AbstractTicketProcessor;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/1/7 2:07 PM
 * @Version 1.0
 */
public abstract class AssetPermissionExtendedAbstractTicketProcessor extends AbstractTicketProcessor<DatasourceInstanceAsset> implements IInstanceType {

    @Resource
    protected DsInstanceAssetService dsInstanceAssetService;

    @Resource
    protected DsConfigHelper dsConfigHelper;

    @Override
    protected Class<DatasourceInstanceAsset> getEntryClassT() {
        return DatasourceInstanceAsset.class;
    }

}
