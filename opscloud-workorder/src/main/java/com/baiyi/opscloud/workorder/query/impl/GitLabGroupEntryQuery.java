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
 * @Date 2022/6/27 11:18
 * @Version 1.0
 */
@Component
public class GitLabGroupEntryQuery extends BaseDsAssetExtendedTicketEntryQuery {

    @Override
    protected DsAssetParam.AssetPageQuery getAssetQueryParam(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        return DsAssetParam.AssetPageQuery.builder()
                .instanceUuid(entryQuery.getInstanceUuid())
                .assetType(DsAssetTypeConstants.GITLAB_GROUP.name())
                .queryName(entryQuery.getQueryName())
                .isActive(true)
                .page(1)
                .length(entryQuery.getLength())
                .build();
    }

    @Override
    protected WorkOrderTicketVO.Entry<DatasourceInstanceAsset> toEntry(WorkOrderTicketEntryParam.EntryQuery entryQuery, DatasourceInstanceAsset entry) {
        WorkOrderTicketVO.Entry<DatasourceInstanceAsset> ticketEntry = super.toEntry(entryQuery, entry);
        ticketEntry.setRole("DEVELOPER");
        ticketEntry.setComment(entry.getDescription());
        return ticketEntry;
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.GITLAB_GROUP.name();
    }

}