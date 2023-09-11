package com.baiyi.opscloud.facade.kubernetes.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.application.ApplicationParam;
import com.baiyi.opscloud.domain.vo.finops.KubernetesFinOpsVO;
import com.baiyi.opscloud.facade.kubernetes.KubernetesFinOpsReportFacade;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetPropertyService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.service.tag.TagService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/8/29 11:34
 * @Version 1.0
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class KubernetesFinOpsReportFacadeImpl implements KubernetesFinOpsReportFacade {

    private final DsInstanceService dsInstanceService;

    private final ApplicationService applicationService;

    private final ApplicationResourceService applicationResourceService;

    private final DsInstanceAssetPropertyService dsInstanceAssetPropertyService;

    private final TagService tagService;

    public KubernetesFinOpsVO.FinOpsReport getKubernetesFinOps(int instanceId) {
        DatasourceInstance datasourceInstance = dsInstanceService.getById(instanceId);
        if (datasourceInstance == null || !datasourceInstance.getInstanceType().equals(DsTypeEnum.KUBERNETES.name())) {
            return KubernetesFinOpsVO.FinOpsReport.EMPTY;
        }
        List<Tag> finOpsTags = tagService.queryFinOpsTags();
        //Map<String, Tag> finOpsTagMap = finOpsTags.stream().collect(Collectors.toMap(Tag::getTagKey, a -> a, (k1, k2) -> k1));
//        Map<String, KubernetesFinOpsVO.FinOpsData> finOpsDataMap = finOpsTags.stream()
//                .map(this::buildFinOpsData)
//                .collect(Collectors.toMap(KubernetesFinOpsVO.FinOpsData::getTag, a -> a, (k1, k2) -> k1));

        List<KubernetesFinOpsVO.FinOpsData> finOpsDataList = Lists.newArrayList();
        int total = 0;
        for (Tag tag : finOpsTags) {
            KubernetesFinOpsVO.FinOpsData finOpsData = buildFinOpsData(tag);
            total = total + finOpsData.getReplicaTotal();
            finOpsDataList.add(finOpsData);
        }

        final int finalTotal = total;
        finOpsDataList.forEach(e -> {
            double rate = (double) e.getReplicaTotal() / finalTotal;
            // 保留2位小数
            DecimalFormat df = new DecimalFormat("##.##%");
            e.setDisplayRate(df.format(rate));
        });

        KubernetesFinOpsVO.FinOpsReport report = KubernetesFinOpsVO.FinOpsReport.builder()
                .data(finOpsDataList)
                .build();
        report.sort();
        return report;
    }

    // 构建FinOpsData
    private KubernetesFinOpsVO.FinOpsData buildFinOpsData(Tag tag) {
        ApplicationParam.ApplicationPageQuery pageQuery = ApplicationParam.ApplicationPageQuery.builder()
                .extend(false)
                .page(1)
                .length(1024)
                .tagId(tag.getId())
                .build();
        DataTable<Application> table = applicationService.queryPageByParam(pageQuery);

        return KubernetesFinOpsVO.FinOpsData.builder()
                .tag(tag.getTagKey())
                .desc(tag.getComment())
                .replicaTotal(countReplicaTotal(table.getData()))
                .build();
    }

    private int countReplicaTotal(List<Application> applications) {
        int total = 0;
        for (Application application : applications) {
            if (!application.getIsActive()) {
                continue;
            }
            List<ApplicationResource> deploymentResources = applicationResourceService.queryByApplication(application.getId(), DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name(), BusinessTypeEnum.ASSET.getType());
            if (!CollectionUtils.isEmpty(deploymentResources)) {
                for (ApplicationResource deploymentResource : deploymentResources) {
                    // 从属性中查询副本数
                    DatasourceInstanceAssetProperty property = dsInstanceAssetPropertyService.getByUniqueKey(deploymentResource.getBusinessId(), "replicas");
                    if (property != null) {
                        try {
                            total = total + Integer.parseInt(property.getValue());
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
            }
        }
        return total;
    }

}

