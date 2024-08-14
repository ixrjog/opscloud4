package com.baiyi.opscloud.leo;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.leo.LeoJobService;
import com.baiyi.opscloud.service.leo.LeoTemplateService;
import com.baiyi.opscloud.service.tag.BusinessTagService;
import com.google.common.base.Splitter;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import jakarta.annotation.Resource;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/9 上午10:09
 * &#064;Version 1.0
 */
public class LeoFeX86ToArm64Test extends BaseKubernetesTest {

    @Resource
    private ApplicationService applicationService;

    @Resource
    private BusinessTagService businessTagService;

    @Resource
    private LeoJobService leoJobService;

    @Resource
    private LeoTemplateService leoTemplateService;

    @Resource
    private ApplicationResourceService applicationResourceService;

    @Test
    void test0() {
        int envType = 2;
        List<Application> applications = applicationService.queryAll();
        for (Application application : applications) {
            if (application.getName().endsWith("-h5")) {
                List<LeoJob> jobs = leoJobService.queryJobWithApplicationId(application.getId()).stream()
                        // 过滤环境 + 有效
                        .filter(e -> e.getEnvType() == envType && e.getIsActive())
                        // 过滤模版是否为 H5模板 id=7
                        .filter(e -> e.getTemplateId() == 7).toList();
                if (!CollectionUtils.isEmpty(jobs)) {
                    System.out.println(application.getName());
                }
            }
        }
    }

    // 查询所有 H5 应用
    @Test
    void test1() {
        List<Application> applications = applicationService.queryAll();
        for (Application application : applications) {
            if (application.getName().endsWith("-h5")) {
                // 查询标签 H5 id=91
                BusinessTag businessTag = BusinessTag.builder()
                        .businessType(BusinessTypeEnum.APPLICATION.getType())
                        .businessId(application.getId())
                        .tagId(91)
                        .build();
                int tagged = businessTagService.countByBusinessTag(businessTag);
                if (tagged == 0) {
                    businessTagService.add(businessTag);
                    System.out.println(StringFormatter.arrayFormat("{}: tagged={}", application.getName(), tagged));
                }
            }
        }
    }

    /**
     * 变更项
     * 1. 任务模版变更 H5模板-ARM64
     * 2. 标签修改@ARM64
     * 2. Deployment 增加污点容忍
     */
    @Test
    void test2() {
        // palmpay-cashbox-h5
        Application application = applicationService.getByName("palmpay-cashbox-h5");
        // dev = 2
        toArm64(application, 1, "dev");
    }

    final String appNames = """
            design-docs-h5
            """;

    @Test
    void test3() {
        Iterable<String> names = Splitter.on("\n").split(appNames);
        names.forEach(e -> {
            if (StringUtils.hasText(e)) {
                Application application = applicationService.getByName(e);
                toArm64(application, 9, "pre");
            }
        });
    }

    void toArm64(Application application, int envType, String envName) {
        List<LeoJob> jobs = leoJobService.queryJobWithApplicationId(application.getId()).stream()
                // 过滤环境 + 有效
                .filter(e -> e.getEnvType() == envType && e.getIsActive())
                // 过滤模版是否为 H5模板 id=7
                .filter(e -> e.getTemplateId() == 7).toList();
        if (CollectionUtils.isEmpty(jobs)) {
            // 无jobs退出
            System.out.println(StringFormatter.format("{} 无有效的Jobs.", application.getName()));
            return;
        }

        for (LeoJob job : jobs) {
            System.out.println(job.getName());
            // step 1
            // 更新 job 中的任务 Template
            LeoTemplate leoTemplate = leoTemplateService.getById(14);
            job.setTemplateId(leoTemplate.getId());
            job.setTemplateContent(leoTemplate.getTemplateContent());
            job.setTemplateVersion("1.0.8");
            // 更新任务内容
            LeoJobModel.JobConfig jobConfig = LeoJobModel.load(job);
            // 修改参数
            for (LeoBaseModel.Parameter parameter : jobConfig.getJob().getParameters()) {
                if ("buildCmd".equals(parameter.getName())) {
                    parameter.setValue("/opt/tools/node-v16.13.0-linux-arm64");
                    continue;
                }
                if ("dockerfile".equals(parameter.getName())) {
                    parameter.setValue("Dockerfile-arm64");
                }
            }
            // step 2 修改标签
            List<String> tags = jobConfig.getJob().getTags().stream().filter(e -> !"@Frankfurt".equals(e)).collect(Collectors.toList());
            tags.add("@ARM64");
            jobConfig.getJob().setTags(tags);
            job.setJobConfig(jobConfig.dump());
            leoJobService.update(job);
            //  LeoJob删除标签
            BusinessTag businessTag = BusinessTag.builder()
                    .businessType(BusinessTypeEnum.LEO_JOB.getType())
                    .businessId(job.getId())
                    .tagId(52)
                    .build();
            businessTag = businessTagService.getByUniqueKey(businessTag);
            if (businessTag != null) {
                businessTagService.deleteById(businessTag.getId());
            }
            // LeoJob新增标签@ARM64
            businessTag = BusinessTag.builder()
                    .businessType(BusinessTypeEnum.LEO_JOB.getType())
                    .businessId(job.getId())
                    .tagId(115)
                    .build();
            if (businessTagService.countByBusinessTag(businessTag) == 0) {
                businessTagService.add(businessTag);
            }
            // 修改Deployment 标签
            List<ApplicationResource> resources = applicationResourceService.queryByApplication(application.getId(), "KUBERNETES_DEPLOYMENT", BusinessTypeEnum.ASSET.getType());

            Optional<ApplicationResource> optional = resources.stream().filter(e -> e.getName().endsWith("-" + envName)).findFirst();
            if (optional.isEmpty()) {
                System.out.println(application.getName() + " 没找到关联的Deployment.");
            } else {
                ApplicationResource resource = optional.get();
                BusinessTag resBusinessTag = BusinessTag.builder()
                        .businessType(BusinessTypeEnum.ASSET.getType())
                        .businessId(resource.getBusinessId())
                        .tagId(52)
                        .build();
                resBusinessTag = businessTagService.getByUniqueKey(resBusinessTag);
                if (resBusinessTag != null) {
                    businessTagService.deleteById(resBusinessTag.getId());
                }
                resBusinessTag = BusinessTag.builder()
                        .businessType(BusinessTypeEnum.ASSET.getType())
                        .businessId(resource.getBusinessId())
                        .tagId(115)
                        .build();
                if (businessTagService.countByBusinessTag(resBusinessTag) == 0) {
                    businessTagService.add(resBusinessTag);
                }
            }
            // step 3 修改无状态
            // ACK-FE
            KubernetesConfig kubernetesConfig = getConfigById(83);
            Deployment deployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(), envName, application.getName() + "-" + envName);
            // 测试
            // deployment.getSpec().getTemplate().getSpec().getAffinity().setNodeAffinity(null);
            // 无状态新增污点容忍
            Optional<NodeAffinity> optionalNodeAffinity = Optional.of(deployment)
                    .map(Deployment::getSpec)
                    .map(DeploymentSpec::getTemplate)
                    .map(PodTemplateSpec::getSpec)
                    .map(PodSpec::getAffinity)
                    .map(Affinity::getNodeAffinity);
            if (optionalNodeAffinity.isEmpty()) {
                NodeAffinity nodeAffinity = new NodeAffinity();
                NodeSelector nodeSelector = new NodeSelector();
                List<NodeSelectorTerm> nodeSelectorTerms = Lists.newArrayList();
                NodeSelectorTerm nodeSelectorTerm = new NodeSelectorTerm();
                List<NodeSelectorRequirement> matchExpressions = Lists.newArrayList();
                NodeSelectorRequirement nodeSelectorRequirement = new NodeSelectorRequirement();
                nodeSelectorRequirement.setKey("kubernetes.io/arch");
                nodeSelectorRequirement.setOperator("In");
                nodeSelectorRequirement.setValues(Lists.newArrayList("arm64"));
                matchExpressions.add(nodeSelectorRequirement);
                nodeSelectorTerm.setMatchExpressions(matchExpressions);
                nodeSelectorTerms.add(nodeSelectorTerm);
                nodeSelector.setNodeSelectorTerms(nodeSelectorTerms);
                nodeAffinity.setRequiredDuringSchedulingIgnoredDuringExecution(nodeSelector);

                if (Optional.of(deployment)
                        .map(Deployment::getSpec)
                        .map(DeploymentSpec::getTemplate)
                        .map(PodTemplateSpec::getSpec)
                        .map(PodSpec::getAffinity).isPresent()) {
                    deployment.getSpec().getTemplate().getSpec().getAffinity().setNodeAffinity(nodeAffinity);
                } else {
                    Affinity affinity = new Affinity();
                    affinity.setNodeAffinity(nodeAffinity);
                    deployment.getSpec().getTemplate().getSpec().setAffinity(affinity);
                }
                try {
                    KubernetesDeploymentDriver.update(kubernetesConfig.getKubernetes(), deployment);
                } catch (Exception e) {
                    System.out.println(StringFormatter.format("{} deployment 更新失败.", application.getName()));
                }
            }
            leoTemplateService.update(leoTemplate);
        }
    }

}
