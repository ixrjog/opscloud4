package com.baiyi.opscloud.workorder.query.impl;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.common.util.StringFormatter;
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
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.entry.ApplicationReduceReplicasEntry;
import com.baiyi.opscloud.workorder.query.impl.base.BaseTicketEntryQuery;
import com.google.common.base.Joiner;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2023/4/10 18:14
 * @Version 1.0
 */
@Component
public class ApplicationReduceReplicasEntryQuery extends BaseTicketEntryQuery<ApplicationReduceReplicasEntry.KubernetesDeployment> {

    @Resource
    private ApplicationResourceService applicationResourceService;

    @Resource
    private ApplicationService applicationService;

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    @Resource
    private DsInstanceAssetPropertyService dsInstanceAssetPropertyService;

    private static final String TICKET_DESC = "(Created: {} Desired: {})";

    @Override
    protected List<ApplicationReduceReplicasEntry.KubernetesDeployment> queryEntries(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        DsAssetParam.AssetPageQuery pageQuery = getAssetQueryParam(entryQuery);
        DataTable<DatasourceInstanceAsset> dataTable = dsInstanceAssetService.queryPageByParam(pageQuery);
        return dataTable.getData().stream().map(e -> {
            Optional<DatasourceInstanceAssetProperty> optionalProperty = dsInstanceAssetPropertyService.queryByAssetId(e.getId())
                    .stream()
                    .filter(p -> p.getName().equals("replicas"))
                    .findFirst();
            int replicas = 0;
            if (optionalProperty.isPresent()) {
                replicas = Integer.parseInt(optionalProperty.get().getValue());
            }
            return ApplicationReduceReplicasEntry.KubernetesDeployment.builder()
                    .id(e.getId())
                    .instanceUuid(entryQuery.getInstanceUuid())
                    .name(e.getAssetId())
                    .deploymentName(e.getAssetKey())
                    .namespace(e.getAssetKey2())
                    .replicas(replicas)
                    .reduceReplicas(replicas - 1)
                    .comment(getApplicationComment(e.getId()))
                    .build();
        }).collect(Collectors.toList());
    }

    private String getApplicationComment(Integer assetId) {
        List<ApplicationResource> resources = applicationResourceService.queryByBusiness(BusinessTypeEnum.ASSET.getType(), assetId);
        return CollectionUtils.isEmpty(resources) ? "" : applicationService.getById(resources.getFirst().getApplicationId()).getComment();
    }

    public static String getComment(ApplicationReduceReplicasEntry.KubernetesDeployment entry) {
        String desc = StringFormatter.arrayFormat(TICKET_DESC, entry.getReplicas(), entry.getReduceReplicas());
        if (StringUtils.isNotBlank(entry.getComment())) {
            return Joiner.on("").skipNulls().join(entry.getComment(), desc);
        } else {
            return desc;
        }
    }

    @Override
    protected WorkOrderTicketVO.Entry<ApplicationReduceReplicasEntry.KubernetesDeployment> toEntry(WorkOrderTicketEntryParam.EntryQuery entryQuery, ApplicationReduceReplicasEntry.KubernetesDeployment entry) {
        return WorkOrderTicketVO.Entry.<ApplicationReduceReplicasEntry.KubernetesDeployment>builder()
                .workOrderTicketId(entryQuery.getWorkOrderTicketId())
                .name(entry.getName())
                .entryKey(entry.getDeploymentName())
                .businessType(BusinessTypeEnum.ASSET.getType())
                .businessId(entry.getId())
                .content(JSONUtil.writeValueAsString(entry))
                .comment(getComment(entry))
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
        return WorkOrderKeyConstants.APPLICATION_REDUCE_REPLICAS.name();
    }

}