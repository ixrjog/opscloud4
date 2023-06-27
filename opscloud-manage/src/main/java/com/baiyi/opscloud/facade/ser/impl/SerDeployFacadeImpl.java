package com.baiyi.opscloud.facade.ser.impl;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.baiyi.opscloud.common.constants.SerDeploySubTaskStatus;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.common.datasource.SerDeployConfig;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.FunctionUtil;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.datasource.aws.s3.driver.AmazonS3Driver;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.SerDeploySubtask;
import com.baiyi.opscloud.domain.generator.opscloud.SerDeployTask;
import com.baiyi.opscloud.domain.generator.opscloud.SerDeployTaskItem;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.param.ser.SerDeployParam;
import com.baiyi.opscloud.domain.vo.ser.SerDeployVO;
import com.baiyi.opscloud.facade.ser.SerDeployFacade;
import com.baiyi.opscloud.packer.ser.SerDeployTaskPacker;
import com.baiyi.opscloud.service.ser.SerDeploySubtaskService;
import com.baiyi.opscloud.service.ser.SerDeployTaskItemService;
import com.baiyi.opscloud.service.ser.SerDeployTaskService;
import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 修远
 * @Date 2023/6/7 11:10 AM
 * @Since 1.0
 */

@Slf4j
@AllArgsConstructor
@Component
public class SerDeployFacadeImpl implements SerDeployFacade {

    private final DsConfigHelper dsConfigHelper;
    private final SerDeployTaskService serDeployTaskService;
    private final SerDeployTaskItemService serDeployTaskItemService;
    private final SerDeploySubtaskService serDeploySubtaskService;
    private final AmazonS3Driver amazonS3Driver;
    private final SerDeployTaskPacker serDeployTaskPacker;

    private final static String S3_OBJECT_MD5_KEY = "ETag";
    private final static String S3_OBJECT_LENGTH_KEY = "Content-Length";
    private final static String S3_OBJECT_RANGES_KEY = "Accept-Ranges";


    private AwsConfig getAwsConfig(String instanceUuid) {
        return dsConfigHelper.build(dsConfigHelper.getConfigByInstanceUuid(instanceUuid), AwsConfig.class);
    }

    private SerDeployConfig getSerDeployConfig() {
        return dsConfigHelper.build(dsConfigHelper.getConfigByDsType(DsTypeEnum.SER_DEPLOY.getType()), SerDeployConfig.class);
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
                    .deployUsername(SessionUtil.getUsername())
                    .build();
            SerDeployTaskItem item = serDeployTaskItemService.getByTaskIdAndItemName(serDeployTask.getId(), file.getOriginalFilename());
            FunctionUtil.isTureOrFalse(ObjectUtils.isEmpty(item))
                    .trueOrFalseHandle(
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
                .taskStatus(SerDeploySubTaskStatus.CREATE)
                .build();
        serDeploySubtaskService.add(subTask);
    }

    @Override
    public void deploySubTask(SerDeployParam.DeploySubTask deploySubTask) {
        SerDeploySubtask subtask = serDeploySubtaskService.getById(deploySubTask.getSerDeploySubTaskId());
        FunctionUtil.isNull(subtask)
                .throwBaseException(new OCException("当前子任务不存在"));
        subtask.setTaskStatus(SerDeploySubTaskStatus.RUNNING);
        serDeploySubtaskService.update(subtask);
        // todo 调用发布
    }
}
