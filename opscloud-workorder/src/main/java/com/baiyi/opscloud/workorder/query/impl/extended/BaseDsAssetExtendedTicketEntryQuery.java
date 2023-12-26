package com.baiyi.opscloud.workorder.query.impl.extended;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.workorder.query.impl.base.BaseTicketEntryQuery;

import jakarta.annotation.Resource;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/11 6:21 PM
 * @Version 1.0
 */
public abstract class BaseDsAssetExtendedTicketEntryQuery extends BaseTicketEntryQuery<DatasourceInstanceAsset> {

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    @Override
    protected List<DatasourceInstanceAsset> queryEntries(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        DsAssetParam.AssetPageQuery pageQuery = getAssetQueryParam(entryQuery);
        DataTable<DatasourceInstanceAsset> dataTable = dsInstanceAssetService.queryPageByParam(pageQuery);
        return dataTable.getData();
    }

    abstract protected DsAssetParam.AssetPageQuery getAssetQueryParam(WorkOrderTicketEntryParam.EntryQuery entryQuery);

    @Override
    protected WorkOrderTicketVO.Entry<DatasourceInstanceAsset> toEntry(WorkOrderTicketEntryParam.EntryQuery entryQuery, DatasourceInstanceAsset entry) {
        return WorkOrderTicketVO.Entry.<DatasourceInstanceAsset>builder()
                .workOrderTicketId(entryQuery.getWorkOrderTicketId())
                .name(entry.getName())
                .entryKey(entry.getName())
                .businessType(BusinessTypeEnum.ASSET.getType())
                .businessId(entry.getId())
                .content(JSONUtil.writeValueAsString(entry))
                .comment(entry.getDescription())
                .entry(entry).build();
    }

}