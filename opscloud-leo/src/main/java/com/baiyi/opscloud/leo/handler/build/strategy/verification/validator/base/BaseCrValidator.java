package com.baiyi.opscloud.leo.handler.build.strategy.verification.validator.base;

import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import com.baiyi.opscloud.core.InstanceHelper;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.leo.handler.build.strategy.verification.validator.factory.CrValidatorFactory;
import com.baiyi.opscloud.leo.constants.BuildDictConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.leo.log.LeoBuildingLog;
import com.baiyi.opscloud.service.sys.EnvService;
import org.springframework.beans.factory.InitializingBean;

import jakarta.annotation.Resource;
import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/11/18 15:07
 * @Version 1.0
 */
public abstract class BaseCrValidator<T extends BaseDsConfig> implements InitializingBean {

    public interface CrTypes {
        String ACR = "ACR";
        String ECR = "ECR";
    }

    @Resource
    protected EnvService envService;

    @Resource
    protected DsConfigManager dsConfigManager;

    @Resource
    private InstanceHelper instanceHelper;

    @Resource
    protected LeoBuildingLog leoLog;

    /**
     * 预检查
     *
     * @param leoJob
     * @param cr
     */
    protected void preInspection(LeoJob leoJob, LeoJobModel.CR cr, LeoBuildModel.BuildConfig buildConfig) {
        preInspection(leoJob, cr);
        Map<String, String> dict = Optional.ofNullable(buildConfig)
                .map(LeoBuildModel.BuildConfig::getBuild)
                .map(LeoBuildModel.Build::getDict)
                .orElseThrow(() -> new LeoBuildException("Configuration does not exist: build->dict"));
        if (!dict.containsKey(BuildDictConstants.IMAGE_TAG.getKey())) {
            throw new LeoBuildException("Configuration does not exist: build->dict->imageTag");
        }
    }

    /**
     * 预检查
     *
     * @param leoJob
     * @param cr
     */

    protected void preInspection(LeoJob leoJob, LeoJobModel.CR cr) {
        LeoJobModel.CRInstance crInstance = Optional.of(cr)
                .map(LeoJobModel.CR::getInstance)
                .orElseThrow(() -> new LeoBuildException("Configuration does not exist: job->cr->instance"));
        Optional.of(crInstance)
                .map(LeoJobModel.CRInstance::getRegionId)
                .orElseThrow(() -> new LeoBuildException("Configuration does not exist: job->cr->instance->regionId"));
        Optional.of(cr)
                .map(LeoJobModel.CR::getRepo)
                .map(LeoJobModel.Repo::getName)
                .orElseThrow(() -> new LeoBuildException("Configuration does not exist: job->cr->repo->name"));
    }

    public void verifyImage(LeoJob leoJob, LeoBuild leoBuild, LeoJobModel.CR cr, LeoBuildModel.BuildConfig buildConfig) {
        // 预检查
        preInspection(leoJob, cr, buildConfig);
        T dsConfig = getDsConfigByUuid(getDsInstanceUuid(cr));
        handleVerifyImage(leoJob, cr, buildConfig, dsConfig);
        leoLog.info(leoBuild, "校验CR镜像成功");
    }

    public void createRepository(LeoJob leoJob, LeoJobModel.CR cr) {
        // 预检查
        preInspection(leoJob, cr);
        T dsConfig = getDsConfigByUuid(getDsInstanceUuid(cr));
        handleCreateRepository(leoJob, cr, dsConfig);
    }

    /**
     * 校验镜像
     *
     * @param leoJob
     * @param cr
     * @param buildConfig
     * @param dsConfig
     */
    protected abstract void handleVerifyImage(LeoJob leoJob, LeoJobModel.CR cr, LeoBuildModel.BuildConfig buildConfig, T dsConfig);

    /**
     * 创建仓库
     * @param leoJob
     * @param cr
     * @param dsConfig
     */
    protected abstract void handleCreateRepository(LeoJob leoJob, LeoJobModel.CR cr, T dsConfig);

    /**
     * 获取配置
     * @param uuid
     * @return
     */
    protected abstract T getDsConfigByUuid(String uuid);

    protected T getDsConfigByUuid(String uuid, Class<T> targetClass) {
        DatasourceConfig dsConfig = dsConfigManager.getConfigByInstanceUuid(uuid);
        return dsConfigManager.build(dsConfig, targetClass);
    }

    /**
     * 查询云
     *
     * @param cr
     * @return
     */
    private String getDsInstanceUuid(LeoJobModel.CR cr) {
        return Optional.of(cr)
                .map(LeoJobModel.CR::getCloud)
                .map(LeoJobModel.Cloud::getUuid)
                .orElseGet(() -> getDsInstanceUuidWithName(cr));
    }

    private String getDsInstanceUuidWithName(LeoJobModel.CR cr) {
        final String name = Optional.of(cr)
                .map(LeoJobModel.CR::getCloud)
                .map(LeoJobModel.Cloud::getName)
                .orElseThrow(() -> new LeoBuildException("任务配置不存在无法验证镜像是否推送成功: job->cr->cloud->name"));
        DatasourceInstance dsInstance = instanceHelper.getInstanceByName(name);
        if (dsInstance == null) {
            throw new LeoBuildException("任务CR数据源实例不存在无法验证镜像是否推送成功！");
        }
        return dsInstance.getUuid();
    }

    /**
     * 取容器注册表类型
     * @return
     */
    public abstract String getCrType();

    /**
     * 注册
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        CrValidatorFactory.register(this);
    }

}