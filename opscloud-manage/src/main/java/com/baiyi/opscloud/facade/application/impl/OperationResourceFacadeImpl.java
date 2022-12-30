package com.baiyi.opscloud.facade.application.impl;

import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.common.constants.OperationConstants;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.common.util.TimeUtil;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.application.ApplicationResourceOperationParam;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.baiyi.opscloud.facade.application.OperationResourceFacade;
import com.baiyi.opscloud.packer.application.ApplicationResourcePacker;
import com.baiyi.opscloud.service.application.ApplicationResourceOperationLogService;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.sys.EnvService;
import com.baiyi.opscloud.service.user.UserPermissionService;
import com.baiyi.opscloud.service.user.UserService;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/7/5 10:19
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OperationResourceFacadeImpl implements OperationResourceFacade {

    private final ApplicationResourceService applicationResourceService;

    private final UserService userService;

    private final UserPermissionService userPermissionService;

    private final ApplicationResourcePacker applicationResourcePacker;

    private final DsInstanceAssetService dsInstanceAssetService;

    private final EnvService envService;

    private final ApplicationResourceOperationLogService applicationResourceOperationLogService;

    private final DsConfigHelper dsConfigHelper;

    private final RedisUtil redisUtil;

    private static final String LOCK_KEY = "opscloud:v4:application:resource:redeploy:resourceId=%s";

    public interface Results {
        String success = "SUCCESS";
        String failed = "FAILED";
    }

    @Override
    public void operationResource(ApplicationResourceOperationParam.OperationResource operationResource) {
        ApplicationResource applicationResource = applicationResourceService.getById(operationResource.getResourceId());
        if (applicationResource == null) throw new OCException(ErrorEnum.APPLICATION_RES_NOT_EXIST);
        String username = SessionUtil.getUsername();
        User user = userService.getByUsername(username);
        boolean isAdmin = isAdmin(user, applicationResource);
        if (applicationResource.getBusinessType() != BusinessTypeEnum.ASSET.getType()) {
            throw new OCException("资产类型不符！");
        }
        DatasourceInstanceAsset asset = dsInstanceAssetService.getById(applicationResource.getBusinessId());
        List<TagVO.Tag> tags = applicationResourcePacker.acqTags(asset);
        Env env = toEnv(tags);
        preInspectionOfProdEnv(env, isAdmin, operationResource);
        ApplicationResourceOperationLog operationLog = recordingOperationLog(user, applicationResource, env, isAdmin, operationResource.getComment());
        try {
            redeploy(asset);
            updateOperationLogResult(operationLog.getId(), Results.success);
        } catch (Exception e) {
            log.error("应用资源操作失败: " + e.getMessage());
            updateOperationLogResult(operationLog.getId(), Results.failed);
        }
    }

    private void preInspectionOfProdEnv(Env env, boolean isAdmin, ApplicationResourceOperationParam.OperationResource operationResource) {
        if (!env.getEnvName().equals(Global.ENV_PROD)) return;
        if (!isAdmin) throw new OCException("应用管理员才能执行生产环境变更操作！");
        if (StringUtils.isBlank(operationResource.getComment())) throw new OCException("生产环境变更操作必须说明变更原因！");

        final String lockKey = String.format(LOCK_KEY, operationResource.getResourceId());
        if (redisUtil.hasKey(lockKey)) {
            throw new OCException("生产环境90秒内禁止重复部署！");
        } else {
            // 加锁
            redisUtil.set(lockKey, true, TimeUtil.secondTime * 90 / 1000);
        }
    }

    /**
     * 重新部署
     *
     * @param asset
     */
    private void redeploy(DatasourceInstanceAsset asset) {
        DatasourceConfig dsConfig = dsConfigHelper.getConfigByInstanceUuid(asset.getInstanceUuid());
        KubernetesConfig kubernetesConfig = dsConfigHelper.build(dsConfig, KubernetesConfig.class);
        String namespace = asset.getAssetKey2();
        String deploymentName = asset.getAssetKey();
        KubernetesDeploymentDriver.redeployDeployment(kubernetesConfig.getKubernetes(), namespace, deploymentName);
    }

    private Env toEnv(List<TagVO.Tag> tags) {
        List<Env> envs = envService.queryAll();
        if (!CollectionUtils.isEmpty(tags)) {
            for (TagVO.Tag tag : tags) {
                Optional<Env> optionalEnv = envs.stream().filter(e -> e.getEnvName().equalsIgnoreCase(tag.getTagKey())).findFirst();
                if (optionalEnv.isPresent()) return optionalEnv.get();
            }
        }
        Optional<Env> optionalEnv = envs.stream().min(Comparator.comparingInt(Env::getEnvType));
        return optionalEnv.orElseGet(() -> Env.builder().envName("default").envType(0).build());
    }

    private boolean isAdmin(User user, ApplicationResource applicationResource) {
        // 判断用户是否授权
        UserPermission queryParam = UserPermission.builder()
                .userId(user.getId())
                .businessType(BusinessTypeEnum.APPLICATION.getType())
                .businessId(applicationResource.getApplicationId())
                .build();
        UserPermission userPermission = userPermissionService.getByUniqueKey(queryParam);
        if (userPermission == null) throw new OCException("非授权用户禁止操作！");
        return "ADMIN".equalsIgnoreCase(userPermission.getPermissionRole());
    }

    /**
     * 记录日志
     */
    private ApplicationResourceOperationLog recordingOperationLog(User user, ApplicationResource applicationResource, Env env, boolean isAdmin, String comment) {
        ApplicationResourceOperationLog operationLog = ApplicationResourceOperationLog.builder()
                .resourceId(applicationResource.getId())
                .resourceType(applicationResource.getResourceType())
                .envType(env.getEnvType())
                .username(user.getUsername()).isAdmin(isAdmin)
                .operationType(OperationConstants.REDEPLOY.name())
                .operationTime(new Date())
                .comment(comment)
                .build();
        applicationResourceOperationLogService.add(operationLog);
        return operationLog;
    }

    private void updateOperationLogResult(Integer id, String result) {
        ApplicationResourceOperationLog operationLog = ApplicationResourceOperationLog.builder()
                .id(id)
                .result(result)
                .build();
        applicationResourceOperationLogService.updateByPrimaryKeySelective(operationLog);
    }

}
