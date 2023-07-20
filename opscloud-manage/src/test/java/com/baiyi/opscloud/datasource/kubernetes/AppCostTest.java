package com.baiyi.opscloud.datasource.kubernetes;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.baiyi.opscloud.domain.base.SimpleBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.baiyi.opscloud.facade.tag.SimpleTagFacade;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.tag.BusinessTagService;
import com.baiyi.opscloud.service.tag.TagService;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2023/1/6 10:50
 * @Version 1.0
 */
public class AppCostTest extends BaseKubernetesTest {

    @Resource
    private ApplicationService applicationService;

    @Resource
    private TagService tagService;

    @Resource
    private BusinessTagService businessTagService;

    @Resource
    private SimpleTagFacade simpleTagFacade;

    @Test
    void eksCostTest() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.EKS_PROD);
        List<Application> applications = applicationService.queryAll();
        for (Application application : applications) {
            String appName = application.getName();
            Deployment deployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), "prod", appName);
            if (deployment == null) continue;

            int replicas = deployment.getSpec().getReplicas();
            SimpleBusiness sb = SimpleBusiness.builder()
                    .businessId(application.getId())
                    .businessType(BusinessTypeEnum.APPLICATION.getType())
                    .build();
            List<TagVO.Tag> tags = simpleTagFacade.queryTagByBusiness(sb);
            List<String> tagNames = tags.stream().map(TagVO.Tag::getTagKey).collect(Collectors.toList());

            String col = "{}\t{}\t{}";
            print(StringFormatter.arrayFormat(col, appName, replicas, JSONUtil.writeValueAsString(tagNames)));
        }
    }

    @Test
    void ackCostTest() {
        KubernetesConfig kubernetesConfig = getConfigById(KubernetesClusterConfigs.ACK_PROD);
        List<Application> applications = applicationService.queryAll();
        for (Application application : applications) {
            String appName = application.getName();
            Deployment deployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), "prod", appName);
            if (deployment == null) continue;

            int replicas = deployment.getSpec().getReplicas();
            SimpleBusiness sb = SimpleBusiness.builder()
                    .businessId(application.getId())
                    .businessType(BusinessTypeEnum.APPLICATION.getType())
                    .build();
            List<TagVO.Tag> tags = simpleTagFacade.queryTagByBusiness(sb);
            List<String> tagNames = tags.stream().map(TagVO.Tag::getTagKey).collect(Collectors.toList());

            String col = "{}\t{}\t{}";
            print(StringFormatter.arrayFormat(col, appName, replicas, JSONUtil.writeValueAsString(tagNames)));
        }
    }
}
