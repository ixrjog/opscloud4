package com.baiyi.opscloud.facade.datasource.resource;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.param.project.ProjectResourceParam;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.project.ProjectResourceVO;
import com.baiyi.opscloud.facade.datasource.DsInstanceAssetFacade;
import com.baiyi.opscloud.factory.resource.base.AbstractProjectResQuery;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

/**
 * @Author 修远
 * @Date 2023/5/19 2:27 PM
 * @Since 1.0
 */

@Slf4j
public class AbstractProjectResQueryWithAsset extends AbstractProjectResQuery {

    @Resource
    private DsInstanceAssetFacade dsInstanceAssetFacade;

    protected static ThreadLocal<ProjectResourceParam.ResourcePageQuery> resourceQuery = new ThreadLocal<>();

    @Override
    public DataTable<ProjectResourceVO.Resource> queryResourcePage(ProjectResourceParam.ResourcePageQuery pageQuery) {
        resourceQuery.set(pageQuery);
        DsAssetParam.AssetPageQuery query = DsAssetParam.AssetPageQuery.builder()
                .instanceId(pageQuery.getInstanceId())
                .instanceUuid(pageQuery.getInstanceUuid())
                .assetType(pageQuery.getProjectResType())
                .queryName(pageQuery.getQueryName())
                .assetKey(pageQuery.getAssetKey())
                .isActive(true)
                .build();
        query.setPage(pageQuery.getPage());
        query.setLength(pageQuery.getLength());
        DataTable<DsAssetVO.Asset> table = dsInstanceAssetFacade.queryAssetPage(query);
        return new DataTable<>(table.getData().stream().map(this::toResource
        ).collect(Collectors.toList()), table.getTotalNum());
    }

    protected ProjectResourceVO.Resource toResource(DsAssetVO.Asset asset) {
        ProjectResourceParam.ResourcePageQuery pageQuery = resourceQuery.get();
        return ProjectResourceVO.Resource.builder()
                .asset(asset)
                // 选择项名称
                .name(getResName(asset))
                // 选择项说明
                .comment(getResComment(asset))
                .projectId(pageQuery.getProjectId())
                .businessId(asset.getBusinessId())
                .businessType(pageQuery.getBusinessType())
                .resourceType(pageQuery.getProjectResType())
                .build();
    }

    /**
     * 可重写
     *
     * @param asset
     * @return
     */
    protected String getResName(DsAssetVO.Asset asset) {
        return asset.getName();
    }

    protected String getResComment(DsAssetVO.Asset asset) {
        return asset.getDescription();
    }
}
