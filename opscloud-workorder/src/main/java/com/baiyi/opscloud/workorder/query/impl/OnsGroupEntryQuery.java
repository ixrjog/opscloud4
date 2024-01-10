package com.baiyi.opscloud.workorder.query.impl;

import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.query.impl.extended.BaseDsAssetExtendedTicketEntryQuery;
import org.springframework.stereotype.Component;

/**
 * @Author 修远
 * @Date 2022/2/11 4:09 PM
 * @Since 1.0
 */
@Component
public class OnsGroupEntryQuery extends BaseDsAssetExtendedTicketEntryQuery {

    @Override
    protected DsAssetParam.AssetPageQuery getAssetQueryParam(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        return DsAssetParam.AssetPageQuery.builder()
                .instanceUuid(entryQuery.getInstanceUuid())
                .assetType(DsAssetTypeConstants.ONS_ROCKETMQ_INSTANCE.name())
                .queryName(entryQuery.getQueryName())
                .isActive(true)
                .page(1)
                .length(entryQuery.getLength())
                .build();
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.ONS_ROCKETMQ_GROUP.name();
    }

}