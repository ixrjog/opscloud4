package com.baiyi.opscloud.workorder.query.impl;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetProperty;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetPropertyService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.entry.KubernetesDeploymentIstioEntry;
import com.baiyi.opscloud.workorder.query.impl.base.BaseTicketEntryQuery;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.datasource.kubernetes.converter.DeploymentAssetConverter.SIDECAR_ISTIO_IO_INJECT;

/**
 * @Author baiyi
 * @Date 2023/12/7 13:17
 * @Version 1.0
 */
@Component
public class KubernetesIstioConfigurationEntryQuery extends BaseTicketEntryQuery<KubernetesDeploymentIstioEntry.KubernetesDeployment> {

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    @Resource
    private DsInstanceAssetPropertyService dsInstanceAssetPropertyService;

    @Override
    protected List<KubernetesDeploymentIstioEntry.KubernetesDeployment> queryEntries(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        DsAssetParam.AssetPageQuery pageQuery = getAssetQueryParam(entryQuery);
        DataTable<DatasourceInstanceAsset> dataTable = dsInstanceAssetService.queryPageByParam(pageQuery);
        return dataTable.getData().stream().map(e -> {
            final String istioInject = dsInstanceAssetPropertyService.queryByAssetId(e.getId())
                    .stream()
                    .filter(p -> p.getName().equals(SIDECAR_ISTIO_IO_INJECT))
                    .findFirst().map(DatasourceInstanceAssetProperty::getValue).orElse("false");

            final String comment = BooleanUtils.toBoolean(istioInject) ? "Disable Istio" : "Enable Istio";
            return KubernetesDeploymentIstioEntry.KubernetesDeployment.builder()
                    .id(e.getId())
                    .instanceUuid(entryQuery.getInstanceUuid())
                    .name(e.getAssetId())
                    .deploymentName(e.getAssetKey())
                    .namespace(e.getAssetKey2())
                    .istioInject(istioInject)
                    //.comment(getApplicationComment(e.getId()))
                    .comment(comment)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    protected WorkOrderTicketVO.Entry<KubernetesDeploymentIstioEntry.KubernetesDeployment> toEntry(WorkOrderTicketEntryParam.EntryQuery entryQuery, KubernetesDeploymentIstioEntry.KubernetesDeployment entry) {
        return WorkOrderTicketVO.Entry.<KubernetesDeploymentIstioEntry.KubernetesDeployment>builder()
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
        return WorkOrderKeyConstants.KUBERNETES_ISTIO_CONFIGURATION.name();
    }

}