package com.baiyi.opscloud.workorder.query.impl;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.query.impl.extended.BaseDsAssetExtendedTicketEntryQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2023/6/7 11:20
 * @Version 1.0
 */
@Component
public class AliyunRamUserEntryQuery extends BaseDsAssetExtendedTicketEntryQuery {

    @Override
    protected DsAssetParam.AssetPageQuery getAssetQueryParam(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        String username = SessionHolder.getUsername();
        return DsAssetParam.AssetPageQuery.builder()
                .instanceUuid(entryQuery.getInstanceUuid())
                .assetType(DsAssetTypeConstants.RAM_USER.name())
                .queryName(username)
                .isActive(true)
                .page(1)
                .length(entryQuery.getLength())
                .build();
    }

    @Override
    protected List<DatasourceInstanceAsset> doFilter(List<DatasourceInstanceAsset> preEntries) {
        String username = SessionHolder.getUsername();
        return preEntries.stream().filter(e -> e.getAssetKey().equals(username)).collect(Collectors.toList());
    }

    @Override
    protected WorkOrderTicketVO.Entry<DatasourceInstanceAsset> toEntry(WorkOrderTicketEntryParam.EntryQuery entryQuery, DatasourceInstanceAsset entry) {
        return WorkOrderTicketVO.Entry.<DatasourceInstanceAsset>builder()
                .workOrderTicketId(entryQuery.getWorkOrderTicketId())
                .name(entry.getAssetKey())
                .entryKey(entry.getName())
                .businessType(BusinessTypeEnum.ASSET.getType())
                .businessId(entry.getId())
                .content(JSONUtil.writeValueAsString(entry))
                .comment(entry.getName())
                .entry(entry).build();
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.ALIYUN_RAM_UPDATE_LOGIN_PROFILE.name();
    }

}