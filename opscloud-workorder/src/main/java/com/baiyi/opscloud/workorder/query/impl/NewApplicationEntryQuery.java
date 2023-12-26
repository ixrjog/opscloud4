package com.baiyi.opscloud.workorder.query.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.entry.NewApplicationEntry;
import com.baiyi.opscloud.workorder.query.impl.base.BaseTicketEntryQuery;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2023/6/5 14:15
 * @Version 1.0
 */
@Component
public class NewApplicationEntryQuery extends BaseTicketEntryQuery<NewApplicationEntry.NewApplication> {

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    @Override
    protected List<NewApplicationEntry.NewApplication> queryEntries(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        DsAssetParam.AssetPageQuery pageQuery = DsAssetParam.AssetPageQuery.builder()
                .instanceUuid(entryQuery.getInstanceUuid())
                .assetType(DsAssetTypeConstants.GITLAB_PROJECT.name())
                .queryName(entryQuery.getQueryName())
                .isActive(true)
                .page(1)
                .length(entryQuery.getLength())
                .build();
        DataTable<DatasourceInstanceAsset> dataTable = dsInstanceAssetService.queryPageByParam(pageQuery);
        return dataTable.getData().stream().map(e ->
                BeanCopierUtil.copyProperties(e, NewApplicationEntry.NewApplication.class)
        ).collect(Collectors.toList());
    }

    @Override
    protected WorkOrderTicketVO.Entry<NewApplicationEntry.NewApplication> toEntry(WorkOrderTicketEntryParam.EntryQuery entryQuery,
                                                                                  NewApplicationEntry.NewApplication entry) {
        return WorkOrderTicketVO.Entry.<NewApplicationEntry.NewApplication>builder()
                .workOrderTicketId(entryQuery.getWorkOrderTicketId())
                .name(entry.getAssetKey())
                .entryKey(entry.getId().toString())
                .businessType(BusinessTypeEnum.ASSET.getType())
                .businessId(entry.getId())
                .content(JSONUtil.writeValueAsString(entry))
                .entry(entry)
                .comment(entry.getDescription())
                .build();
    }


    @Override
    public String getKey() {
        return WorkOrderKeyConstants.NEW_APPLICATION.name();
    }

}