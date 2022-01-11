package com.baiyi.opscloud.workorder.processor.impl.extended;

import com.baiyi.opscloud.domain.base.IInstanceType;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.workorder.exception.VerifyTicketEntryException;
import com.baiyi.opscloud.workorder.processor.impl.base.BaseTicketProcessor;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/1/7 2:07 PM
 * @Version 1.0
 */
public abstract class AbstractDatasourceAssetPermissionExtendedBaseTicketProcessor extends BaseTicketProcessor<DatasourceInstanceAsset> implements IInstanceType {

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    protected DatasourceInstanceAsset getAsset(DatasourceInstanceAsset queryParam) throws VerifyTicketEntryException {
        try {
            DatasourceInstanceAsset asset = dsInstanceAssetService.getByUniqueKey(queryParam);
            if (asset == null)
                throw new VerifyTicketEntryException("校验工单条目失败: 授权资产不存在!");
            return asset;
        } catch (Exception e) {
            throw new VerifyTicketEntryException("查询授权资产错误: " + e.getMessage());
        }
    }

    @Override
    protected Class<DatasourceInstanceAsset> getEntryClassT() {
        return DatasourceInstanceAsset.class;
    }

}
