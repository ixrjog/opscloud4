package com.baiyi.opscloud.workorder.query.impl;

import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.query.impl.extended.DatasourceAssetExtendedTicketEntryQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2023/4/18 14:17
 * @Version 1.0
 */
@Component
public class AwsIamUserEntryQuery extends DatasourceAssetExtendedTicketEntryQuery {

    @Override
    protected DsAssetParam.AssetPageQuery getAssetQueryParam(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        String username = SessionUtil.getUsername();
        return DsAssetParam.AssetPageQuery.builder()
                .instanceUuid(entryQuery.getInstanceUuid())
                .assetType(DsAssetTypeConstants.IAM_USER.name())
                .queryName(username)
                .isActive(true)
                .page(1)
                .length(entryQuery.getLength())
                .build();
    }

    @Override
    protected List<DatasourceInstanceAsset> doFilter(List<DatasourceInstanceAsset> preEntries) {
        String username = SessionUtil.getUsername();
        return preEntries.stream().filter(e -> e.getName().equals(username)).collect(Collectors.toList());
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.AWS_IAM_UPDATE_LOGIN_PROFILE.name();
    }

}
