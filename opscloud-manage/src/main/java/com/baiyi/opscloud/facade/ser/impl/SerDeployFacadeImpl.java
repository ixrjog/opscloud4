package com.baiyi.opscloud.facade.ser.impl;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.baiyi.opscloud.common.base.AccessLevel;
import com.baiyi.opscloud.common.constants.SerDeployConstants;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.common.datasource.SerDeployConfig;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.feign.driver.RiskControlDriver;
import com.baiyi.opscloud.common.feign.request.RiskControlRequest;
import com.baiyi.opscloud.common.feign.response.MgwCoreResponse;
import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.common.holder.WorkOrderSerDeployHolder;
import com.baiyi.opscloud.common.util.*;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.datasource.aws.s3.driver.AmazonS3Driver;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.param.ser.SerDeployParam;
import com.baiyi.opscloud.domain.vo.ser.SerDeployVO;
import com.baiyi.opscloud.facade.auth.PlatformAuthValidator;
import com.baiyi.opscloud.facade.ser.SerDeployFacade;
import com.baiyi.opscloud.packer.ser.SerDeployTaskPacker;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.auth.AuthRoleService;
import com.baiyi.opscloud.service.ser.SerDeploySubtaskCallbackService;
import com.baiyi.opscloud.service.ser.SerDeploySubtaskService;
import com.baiyi.opscloud.service.ser.SerDeployTaskItemService;
import com.baiyi.opscloud.service.ser.SerDeployTaskService;
import com.baiyi.opscloud.service.sys.EnvService;
import com.google.common.base.Joiner;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.common.base.Global.ENV_PROD;

/**
 * @Author 修远
 * @Date 2023/6/7 11:10 AM
 * @Since 1.0
 */

@Slf4j
@AllArgsConstructor
@Component
public class SerDeployFacadeImpl implements SerDeployFacade {

    private final DsConfigManager dsConfigManager;
    private final SerDeployTaskService serDeployTaskService;
    private final SerDeployTaskItemService serDeployTaskItemService;
    private final SerDeploySubtaskService serDeploySubtaskService;
    private final AmazonS3Driver amazonS3Driver;
    private final SerDeployTaskPacker serDeployTaskPacker;
    private final EnvService envService;
    private final ApplicationService applicationService;
    private final PlatformAuthValidator platformAuthHelper;
    private final SerDeploySubtaskCallbackService SerDeploySubtaskCallbackService;
    private final WorkOrderSerDeployHolder workOrderSerDeployHolder;
    private final AuthRoleService authRoleService;

    private final static String SER_SUFFIX = ".ser";
    private final static String S3_OBJECT_MD5_KEY = "ETag";
    private final static String S3_OBJECT_LENGTH_KEY = "Content-Length";
    private final static String S3_OBJECT_RANGES_KEY = "Accept-Ranges";


    private AwsConfig getAwsConfig(String instanceUuid) {
        return dsConfigManager.build(dsConfigManager.getConfigByInstanceUuid(instanceUuid), AwsConfig.class);
    }

    private SerDeployConfig getSerDeployConfig() {
        return dsConfigManager.build(dsConfigManager.getConfigByDsType(DsTypeEnum.SER_DEPLOY.getType()), SerDeployConfig.class);
    }

    @Override
    public void uploadFile(MultipartFile file, String taskUuid) {
        SerDeployTask serDeployTask = serDeployTaskService.getByUuid(taskUuid);
        if (!CollectionUtils.isEmpty(serDeploySubtaskService.listBySerDeployTaskId(serDeployTask.getId()))) {
            return;
        }
        SerDeployConfig serDeployConfig = getSerDeployConfig();
        AwsConfig.Aws awsConfig = getAwsConfig(serDeployConfig.getSerDeployInstance().getInstanceUuid()).getAws();
        try (InputStream inputStream = file.getInputStream()) {
            String keyName = Joiner.on("/").join(taskUuid, file.getOriginalFilename());
            String bucketName = serDeployConfig.getSerDeployInstance().getBucketName();
            String versionId = amazonS3Driver.putObject(serDeployConfig.getSerDeployInstance().getRegionId(), awsConfig, bucketName, keyName, inputStream, new ObjectMetadata());
            S3Object s3Object = amazonS3Driver.getObject(serDeployConfig.getSerDeployInstance().getRegionId(), awsConfig, bucketName, keyName, versionId);
            String itemSize = Joiner.on(" ")
                    .join(s3Object.getObjectMetadata().getRawMetadata().get(S3_OBJECT_LENGTH_KEY),
                            s3Object.getObjectMetadata().getRawMetadata().get(S3_OBJECT_RANGES_KEY));
            SerDeployTaskItem serDeployTaskItem = SerDeployTaskItem.builder()
                    .serDeployTaskId(serDeployTask.getId())
                    .itemName(file.getOriginalFilename())
                    .itemKey(keyName)
                    .itemBucketName(bucketName)
                    .itemMd5(s3Object.getObjectMetadata().getRawMetadata().get(S3_OBJECT_MD5_KEY).toString())
                    .itemSize(itemSize)
                    .reloadUsername(SessionHolder.getUsername())
                    .build();
            SerDeployTaskItem item = serDeployTaskItemService.getByTaskIdAndItemName(serDeployTask.getId(), file.getOriginalFilename());
            FunctionUtil.isTureOrFalse(ObjectUtils.isEmpty(item))
                    .withBoolean(
                            () -> serDeployTaskItemService.add(serDeployTaskItem),
                            () -> {
                                serDeployTaskItem.setId(item.getId());
                                serDeployTaskItemService.update(serDeployTaskItem);
                            }
                    );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DataTable<SerDeployVO.Task> querySerDeployTaskPage(SerDeployParam.TaskPageQuery pageQuery) {
        DataTable<SerDeployTask> table = serDeployTaskService.queryPageByParam(pageQuery);
        List<SerDeployVO.Task> data = BeanCopierUtil.copyListProperties(table.getData(), SerDeployVO.Task.class)
                .stream()
                .peek(e -> serDeployTaskPacker.wrap(e, pageQuery))
                .collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public SerDeployVO.Task getSerDeployTaskByUuid(SerDeployParam.QueryByUuid param) {
        SerDeployTask serDeployTask = serDeployTaskService.getByUuid(param.getTaskUuid());
        FunctionUtil.isNull(serDeployTask)
                .throwBaseException(new OCException("Ser 包发布任务不存在"));
        SerDeployVO.Task taskVO = BeanCopierUtil.copyProperties(serDeployTask, SerDeployVO.Task.class);
        serDeployTaskPacker.wrap(taskVO, SimpleExtend.EXTEND);
        return taskVO;
    }

    @Override
    public void addSerDeployTask(SerDeployParam.AddTask addTask) {
        FunctionUtil.isNotNull(serDeployTaskService.getByName(addTask.getTaskName()))
                .throwBaseException(new OCException("发布任务名称重复"));
        SerDeployTask task = SerDeployTask.builder()
                .applicationId(addTask.getApplicationId())
                .taskUuid(IdUtil.buildUUID())
                .taskName(addTask.getTaskName())
                .taskDesc(addTask.getTaskDesc())
                .isActive(addTask.getIsActive())
                .isFinish(addTask.getIsFinish())
                .build();
        serDeployTaskService.add(task);
    }

    @Override
    public void updateSerDeployTask(SerDeployParam.UpdateTask updateTask) {
        FunctionUtil.isNull(serDeployTaskService.getById(updateTask.getId()))
                .throwBaseException(new OCException("Ser 包发布任务不存在"));
        SerDeployTask task = BeanCopierUtil.copyProperties(updateTask, SerDeployTask.class);
        serDeployTaskService.updateKeySelective(task);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteSerDeployTaskItem(Integer id) {
        SerDeployTaskItem item = serDeployTaskItemService.getById(id);
        FunctionUtil.isNull(item)
                .throwBaseException(new OCException("Item 项不存在"));
        List<SerDeploySubtask> subtaskList = serDeploySubtaskService.listBySerDeployTaskId(item.getSerDeployTaskId());
        FunctionUtil.isTure(!CollectionUtils.isEmpty(subtaskList))
                .throwBaseException(new OCException("当前Item 项已被发布过，无法删除"));
        SerDeployConfig serDeployConfig = getSerDeployConfig();
        AwsConfig.Aws awsConfig = getAwsConfig(serDeployConfig.getSerDeployInstance().getInstanceUuid()).getAws();
        amazonS3Driver.deleteObject(serDeployConfig.getSerDeployInstance().getRegionId(), awsConfig, item.getItemBucketName(), item.getItemKey());
        serDeployTaskItemService.deleteById(id);
    }

    @Override
    public void addSerDeploySubTask(SerDeployParam.AddSubTask addSubTask) {
        FunctionUtil.isTure(CollectionUtils.isEmpty(serDeployTaskItemService.listBySerDeployTaskId(addSubTask.getSerDeployTaskId())))
                .throwBaseException(new OCException("当前任务无关联的 Ser 包"));
        SerDeploySubtask serDeploySubtask = serDeploySubtaskService.getByTaskIdAndEnvType(addSubTask.getSerDeployTaskId(), addSubTask.getEnvType());
        FunctionUtil.isNotNull(serDeploySubtask)
                .throwBaseException(new OCException("当前环境子任务已存在"));
        SerDeploySubtask subTask = SerDeploySubtask.builder()
                .serDeployTaskId(addSubTask.getSerDeployTaskId())
                .envType(addSubTask.getEnvType())
                .taskStatus(SerDeployConstants.SubTaskStatus.CREATE)
                .build();
        serDeploySubtaskService.add(subTask);
    }

    private SerDeploySubtask validDeploySubTask(Integer serDeploySubTaskId) {
        SerDeploySubtask subtask = serDeploySubtaskService.getById(serDeploySubTaskId);
        FunctionUtil.isNull(subtask)
                .throwBaseException(new OCException("当前子任务不存在"));
        return subtask;
    }

    @Override
    public void deploySubTask(SerDeployParam.DeploySubTask deploySubTask) {
        SerDeploySubtask subtask = validDeploySubTask(deploySubTask.getSerDeploySubTaskId());
        Env env = envService.getByEnvType(subtask.getEnvType());
        FunctionUtil.isNull(env)
                .throwBaseException(new OCException("选择环境不存在"));
        envValid(subtask, env);
        subtask.setDeployUsername(SessionHolder.getUsername());
        serDeploySubtaskService.update(subtask);
        try {
            SerDeployConfig serDeployConfig = getSerDeployConfig();
            String url = serDeployConfig.getSerDeployURI().get(env.getEnvName());
            FunctionUtil.isNullOrEmpty(url)
                    .throwBaseException(new OCException("选择环境调用 URL 未配置"));
            SerDeployTask task = serDeployTaskService.getById(subtask.getSerDeployTaskId());
            Application application = applicationService.getById(task.getApplicationId());
            RiskControlRequest.SerLoader request = buildRequest(task, subtask);
            subtask.setStartTime(new Date());
            MgwCoreResponse<?> response = RiskControlDriver.serReload(url, application.getName(), request);
            subtask.setRequestContent(JSONUtil.writeValueAsString(request));
            subtask.setResponseContent(JSONUtil.writeValueAsString(response));
            if (response.isSuccess()) {
                SerDeploySubtaskCallbackService.deleteByBySerDeploySubtaskId(subtask.getId());
                subtask.setTaskStatus(SerDeployConstants.SubTaskStatus.RELOADING);
                subtask.setTaskResult(Strings.EMPTY);
                subtask.setEndTime(null);
            } else {
                subtask.setTaskStatus(SerDeployConstants.SubTaskStatus.FINISH);
                subtask.setEndTime(new Date());
                subtask.setTaskResult(SerDeployConstants.SubTaskResult.CALL_FAIL);
            }
            serDeploySubtaskService.update(subtask);
        } catch (OCException ocException) {
            subtask.setTaskStatus(SerDeployConstants.SubTaskStatus.FINISH);
            subtask.setEndTime(new Date());
            subtask.setTaskResult(SerDeployConstants.SubTaskResult.VALID_FAIL);
            serDeploySubtaskService.update(subtask);
            throw ocException;
        } catch (FeignException feignException) {
            subtask.setTaskStatus(SerDeployConstants.SubTaskStatus.FINISH);
            subtask.setEndTime(new Date());
            subtask.setTaskResult(SerDeployConstants.SubTaskResult.VALID_FAIL);
            subtask.setResponseContent(feignException.getMessage());
            serDeploySubtaskService.update(subtask);
            throw new OCException(feignException.getMessage());
        }
    }

    private void envValid(SerDeploySubtask subtask, Env env) {
        int accessLevel = authRoleService.getRoleAccessLevelByUsername(SessionHolder.getUsername());
        if (accessLevel >= AccessLevel.OPS.getLevel()) {
            return;
        }
        if (ENV_PROD.equals(env.getEnvName())) {
            FunctionUtil.isTure(!workOrderSerDeployHolder.hasKey(subtask.getSerDeployTaskId()))
                    .throwBaseException(new OCException("生产环境请先提交工单后发布"));
        }
    }

    private RiskControlRequest.SerLoader buildRequest(SerDeployTask task, SerDeploySubtask subtask) {
        List<RiskControlRequest.ReloadedSer> serList = serDeployTaskItemService.listBySerDeployTaskId(task.getId()).stream()
                .map(serDeployTaskItem ->
                        RiskControlRequest.ReloadedSer.builder()
                                .serName(serDeployTaskItem.getItemName())
                                .bucketName(serDeployTaskItem.getItemBucketName())
                                .keyName(serDeployTaskItem.getItemKey())
                                .fileMd5(serDeployTaskItem.getItemMd5())
                                .build()
                ).toList();
        return RiskControlRequest.SerLoader.builder()
                .operator(SessionHolder.getUsername())
                .taskNo(subtask.getId())
                .reloadingSerList(serList)
                .build();
    }

    @Override
    public void deploySubTaskCallback(SerDeployParam.DeploySubTaskCallback callback) {
        platformAuthHelper.verify(callback);
        SerDeploySubtask subtask = validDeploySubTask(callback.getSerDeploySubTaskId());
        SerDeploySubtaskCallback subtaskCallback = SerDeploySubtaskCallback.builder()
                .serDeploySubtaskId(callback.getSerDeploySubTaskId())
                .callbackContent(callback.getContent())
                .build();
        SerDeploySubtaskCallbackService.add(subtaskCallback);
        if (!SerDeployConstants.SubTaskResult.SUCCESS.equals(subtask.getTaskResult())) {
            subtask.setTaskStatus(SerDeployConstants.SubTaskStatus.FINISH);
            subtask.setTaskResult(SerDeployConstants.SubTaskResult.SUCCESS);
            subtask.setEndTime(new Date());
            serDeploySubtaskService.update(subtask);
        }
    }

    @Override
    public List<SerDeployVO.SerDetail> queryCurrentSer(SerDeployParam.QueryCurrentSer queryCurrentSer) {
        SerDeployConfig serDeployConfig = getSerDeployConfig();
        AwsConfig.Aws awsConfig = getAwsConfig(serDeployConfig.getSerDeployInstance().getInstanceUuid()).getAws();
        Object[] objects = {queryCurrentSer.getApplicationName(), queryCurrentSer.getEnvName()};
        String prefix = StringFormatter.arrayFormat(serDeployConfig.getCurrentSerPath(), objects);
        SerDeployConfig.SerDeployInstance serDeployInstance = serDeployConfig.getSerDeployInstance();
        return amazonS3Driver.listObjects(serDeployInstance.getRegionId(), awsConfig, serDeployInstance.getBucketName(), prefix).stream()
                .filter(s3ObjectSummary -> s3ObjectSummary.getKey().endsWith(SER_SUFFIX))
                .map(s3ObjectSummary -> SerDeployVO.SerDetail.builder()
                        .serName(s3ObjectSummary.getKey().split("/")[2])
                        .serMd5(s3ObjectSummary.getETag())
                        .serSize(s3ObjectSummary.getSize())
                        .lastModified(s3ObjectSummary.getLastModified())
                        .isLastHalfHour(System.currentTimeMillis() - s3ObjectSummary.getLastModified().getTime() < NewTimeUtil.MINUTE_TIME * 30)
                        .build()
                ).toList();
    }

}