package com.baiyi.opscloud.workorder.query.impl;

import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.query.impl.extended.BaseDsAssetExtendedTicketEntryQuery;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/1/13 7:35 PM
 * @Version 1.0
 */
@Component
public class NacosEntryQuery extends BaseDsAssetExtendedTicketEntryQuery {

    @Override
    protected DsAssetParam.AssetPageQuery getAssetQueryParam(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        return DsAssetParam.AssetPageQuery.builder()
                .instanceUuid(entryQuery.getInstanceUuid())
                .assetType(DsAssetTypeConstants.NACOS_PERMISSION.name())
                .queryName(entryQuery.getQueryName())
                .isActive(true)
                .page(1)
                .length(entryQuery.getLength())
                .build();
    }

    @Override
    protected WorkOrderTicketVO.Entry<DatasourceInstanceAsset> toEntry(WorkOrderTicketEntryParam.EntryQuery entryQuery, DatasourceInstanceAsset entry) {
        WorkOrderTicketVO.Entry<DatasourceInstanceAsset> ticketEntry = super.toEntry(entryQuery, entry);
        ticketEntry.setComment("Nacos开发者权限");
        return ticketEntry;
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.NACOS.name();
    }

}