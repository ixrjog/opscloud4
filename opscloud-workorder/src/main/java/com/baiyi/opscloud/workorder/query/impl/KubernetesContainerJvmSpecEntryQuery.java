package com.baiyi.opscloud.workorder.query.impl;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.ApplicationResource;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetProperty;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetPropertyService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.workorder.constants.JvmSpecConstants;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.entry.KubernetesContainerJvmSpecEntry;
import com.baiyi.opscloud.workorder.query.impl.base.BaseTicketEntryQuery;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2023/11/3 09:46
 * @Version 1.0
 */
@Component
public class KubernetesContainerJvmSpecEntryQuery extends BaseTicketEntryQuery<KubernetesContainerJvmSpecEntry.KubernetesDeployment> {

    @Resource
    private ApplicationResourceService applicationResourceService;

    @Resource
    private ApplicationService applicationService;

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    @Resource
    private DsInstanceAssetPropertyService dsInstanceAssetPropertyService;

    private static final String DEF_SPECIFICATIONS = JvmSpecConstants.XLARGE.name();

    @Override
    protected List<KubernetesContainerJvmSpecEntry.KubernetesDeployment> queryEntries(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        DsAssetParam.AssetPageQuery pageQuery = getAssetQueryParam(entryQuery);
        DataTable<DatasourceInstanceAsset> dataTable = dsInstanceAssetService.queryPageByParam(pageQuery);
        return dataTable.getData().stream().map(e -> {
            final String spec = dsInstanceAssetPropertyService.queryByAssetId(e.getId())
                    .stream()
                    .filter(p -> p.getName().equals("spec"))
                    .findFirst().map(DatasourceInstanceAssetProperty::getValue).orElse(DEF_SPECIFICATIONS);
            return KubernetesContainerJvmSpecEntry.KubernetesDeployment.builder()
                    .id(e.getId())
                    .instanceUuid(entryQuery.getInstanceUuid())
                    .name(e.getAssetId())
                    .deploymentName(e.getAssetKey())
                    .namespace(e.getAssetKey2())
                    .spec(spec)
                    .comment(getApplicationComment(e.getId()))
                    .build();
        }).collect(Collectors.toList());
    }

    private String getApplicationComment(Integer assetId) {
        List<ApplicationResource> resources = applicationResourceService.queryByBusiness(BusinessTypeEnum.ASSET.getType(), assetId);
        return CollectionUtils.isEmpty(resources) ? "" : applicationService.getById(resources.getFirst().getApplicationId()).getComment();
    }

    @Override
    protected WorkOrderTicketVO.Entry<KubernetesContainerJvmSpecEntry.KubernetesDeployment> toEntry(WorkOrderTicketEntryParam.EntryQuery entryQuery, KubernetesContainerJvmSpecEntry.KubernetesDeployment entry) {
        return WorkOrderTicketVO.Entry.<KubernetesContainerJvmSpecEntry.KubernetesDeployment>builder()
                .workOrderTicketId(entryQuery.getWorkOrderTicketId())
                .name(entry.getName())
                .entryKey(entry.getDeploymentName())
                .businessType(BusinessTypeEnum.ASSET.getType())
                .businessId(entry.getId())
                .content(JSONUtil.writeValueAsString(entry))
                .comment(entry.getComment())
                .entry(entry)
                .build();
    }

    private DsAssetParam.AssetPageQuery getAssetQueryParam(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        return DsAssetParam.AssetPageQuery.builder()
                .instanceUuid(entryQuery.getInstanceUuid())
                .assetType(DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name())
                .queryName(entryQuery.getQueryName())
                .isActive(true)
                .page(1)
                .length(entryQuery.getLength())
                .build();
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.KUBERNETES_CONTAINER_JVM_SPEC_CHANGES.name();
    }

}