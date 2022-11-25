package com.baiyi.opscloud.leo.build.concrete.post.verify;

import com.aliyuncs.cr.model.v20181201.ListRepoTagResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.acr.driver.AliyunAcrImageDriver;
import com.baiyi.opscloud.datasource.aliyun.acr.driver.AliyunAcrRepositoryDriver;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.leo.build.concrete.post.verify.base.BaseCrImageValidator;
import com.baiyi.opscloud.leo.constants.BuildDictConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.baiyi.opscloud.leo.build.concrete.post.verify.base.BaseCrImageValidator.CRS.ACR;

/**
 * @Author baiyi
 * @Date 2022/11/18 15:12
 * @Version 1.0
 */
@Slf4j
@Component
public class AcrImageValidator extends BaseCrImageValidator<AliyunConfig> {

    @Resource
    private AliyunAcrRepositoryDriver aliyunAcrRepositoryDriver;

    @Resource
    private AliyunAcrImageDriver aliyunAcrImageDriver;

    @Override
    public String getCrType() {
        return ACR;
    }

    @Override
    protected AliyunConfig getDsConfigByUuid(String uuid) {
        return getDsConfigByUuid(uuid, AliyunConfig.class);
    }

    @Override
    protected void handleVerify(LeoJob leoJob, LeoJobModel.CR cr, LeoBuildModel.BuildConfig buildConfig, AliyunConfig dsConfig) {
        LeoJobModel.CRInstance crInstance = cr.getInstance();
        final String crRegionId = cr.getInstance().getRegionId();
        final String crInstanceId = crInstance.getId();
        final String crRepoName = cr.getRepo().getName();
        Map<String, String> dict = buildConfig.getBuild().getDict();
        final String imageTag = dict.get(BuildDictConstants.IMAGE_TAG.getKey());
        final String envName = dict.get(BuildDictConstants.ENV.getKey());
        final String crRepoId = Optional.of(cr)
                .map(LeoJobModel.CR::getRepo)
                .map(LeoJobModel.Repo::getId)
                .orElseGet(() -> getCrRepoId(cr, leoJob, crRegionId, crInstanceId, crRepoName, dsConfig, envName));
        try {
            List<ListRepoTagResponse.ImagesItem> imagesItems = aliyunAcrImageDriver.listImage(crRegionId, dsConfig.getAliyun(), crInstanceId, crRepoId, QUERY_IMAGES_SIZE);
            imagesItems.stream().filter(image -> image.getTag().equals(imageTag))
                    .findFirst()
                    .orElseThrow(() -> new LeoBuildException("阿里云ACR中未找到构建镜像: repoId={}, imageTag={}", crRepoId, imageTag));
        } catch (ClientException e) {
            throw new LeoBuildException("查询阿里云-ACR镜像错误: err={}", e.getMessage());
        }
    }

    private String getCrRepoId(LeoJobModel.CR cr, LeoJob leoJob, String crRegionId, String crInstanceId, String crRepoName, AliyunConfig dsConfig, String envName) {
        final String repoNamespace = Optional.of(cr)
                .map(LeoJobModel.CR::getRepo)
                .map(LeoJobModel.Repo::getNamespace)
                .orElse(envName);
        try {
            return aliyunAcrRepositoryDriver.getRepositoryId(crRegionId, dsConfig.getAliyun(), crInstanceId, repoNamespace, crRepoName);
        } catch (ClientException e) {
            throw new LeoBuildException("任务CR repoId查询失败: regionId={}, instanceId={}, repoNamespace={}, repoName={}", crRegionId, crInstanceId, repoNamespace, crRepoName);
        }
    }

}
