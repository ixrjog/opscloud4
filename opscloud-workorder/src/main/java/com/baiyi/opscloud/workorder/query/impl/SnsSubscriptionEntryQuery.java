package com.baiyi.opscloud.workorder.query.impl;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.query.impl.extended.BaseDsAssetExtendedTicketEntryQuery;
import org.springframework.stereotype.Component;

/**
 * @Author 修远
 * @Date 2022/4/6 5:17 PM
 * @Since 1.0
 */

@Component
public class SnsSubscriptionEntryQuery extends BaseDsAssetExtendedTicketEntryQuery {

    @Override
    protected DsAssetParam.AssetPageQuery getAssetQueryParam(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        return DsAssetParam.AssetPageQuery.builder()
                .instanceUuid(entryQuery.getInstanceUuid())
                .regionId(entryQuery.getRegionId())
                .kind(entryQuery.getKind())
                .assetType(entryQuery.getAssetType())
                .queryName(entryQuery.getQueryName())
                .isActive(true)
                .page(1)
                .length(entryQuery.getLength())
                .build();
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.SNS_SUBSCRIPTION.name();
    }

    @Override
    protected WorkOrderTicketVO.Entry<DatasourceInstanceAsset> toEntry(WorkOrderTicketEntryParam.EntryQuery entryQuery, DatasourceInstanceAsset entry) {
        return WorkOrderTicketVO.Entry.<DatasourceInstanceAsset>builder()
                .workOrderTicketId(entryQuery.getWorkOrderTicketId())
                .name(entry.getName())
                .entryKey(entry.getKind())
                .businessType(BusinessTypeEnum.ASSET.getType())
                .businessId(entry.getId())
                .content(entry.getAssetKey2())
                .build();
    }

}