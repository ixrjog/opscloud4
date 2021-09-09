package com.baiyi.opscloud.facade.datasource.resource;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.application.ApplicationResourceParam;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.facade.datasource.DsInstanceFacade;
import com.baiyi.opscloud.factory.resource.base.AbstractApplicationResourceQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/9/9 10:49 上午
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractAssetResourceQuery extends AbstractApplicationResourceQuery {

    @Autowired
    private DsInstanceFacade dsInstanceFacade;

    protected static ThreadLocal<ApplicationResourceParam.ResourcePageQuery> resourceQuery = new ThreadLocal<>();

    @Override
    public DataTable<ApplicationResourceVO.Resource> queryResourcePage(ApplicationResourceParam.ResourcePageQuery pageQuery) {
        resourceQuery.set(pageQuery);
        DsAssetParam.AssetPageQuery query = DsAssetParam.AssetPageQuery.builder()
                .instanceId(pageQuery.getInstanceId())
                .instanceUuid(pageQuery.getInstanceUuid())
                .assetType(pageQuery.getApplicationResType())
                .queryName(pageQuery.getQueryName())
                .isActive(true)
                .build();
        query.setPage(pageQuery.getPage());
        query.setLength(pageQuery.getLength());
        DataTable<DsAssetVO.Asset> table = dsInstanceFacade.queryAssetPage(query);
        return new DataTable<>(table.getData().stream().map(this::toResource
        ).collect(Collectors.toList()), table.getTotalNum());
    }

    protected ApplicationResourceVO.Resource toResource(DsAssetVO.Asset asset) {
        ApplicationResourceParam.ResourcePageQuery pageQuery = resourceQuery.get();
        return ApplicationResourceVO.Resource.builder()
                .name(asset.getName())
                .applicationId(pageQuery.getApplicationId())
                .businessId(asset.getBusinessId())
                .businessType(pageQuery.getBusinessType())
                .resourceType(pageQuery.getApplicationResType())
                .comment(asset.getAssetId())
                .build();
    }

}
