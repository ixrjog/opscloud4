package com.baiyi.opscloud.facade.ser.impl;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.core.InstanceHelper;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.datasource.aws.s3.driver.AmazonS3Driver;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.SerDeployTask;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    private final InstanceHelper instanceHelper;

    private final SerDeployTaskService serDeployTaskService;

    private final SerDeployTaskItemService serDeployTaskItemService;

    private final SerDeploySubtaskService serDeploySubtaskService;

    private final AmazonS3Driver amazonS3Driver;

    private final DsConfigHelper dsConfigHelper;

    private final SerDeployTaskPacker serDeployTaskPacker;

    private static final String DEFAULT_REGION_ID = "eu-west-1";

    private static final String SER_FILE_SUFFIX = "ser";
    private static final String FILTER_INSTANCE_TAG = "ser";
    private static final DsTypeEnum[] FILTER_INSTANCE_TYPES = {DsTypeEnum.AWS};

    private AwsConfig getConfigById(List<DatasourceInstance> instances) {
        return dsConfigHelper.build(dsConfigHelper.getConfigById(instances.get(0).getId()), AwsConfig.class);
    }

    @Override
    public void uploadFile(MultipartFile file, String taskUuid) {
        List<DatasourceInstance> instances = instanceHelper.listInstance(FILTER_INSTANCE_TYPES, FILTER_INSTANCE_TAG);
        if (!CollectionUtils.isEmpty(instances)) {
            AwsConfig.Aws awsConfig = getConfigById(instances).getAws();
            try {
                InputStream inputStream = file.getInputStream();
                String keyName = Joiner.on("/").join(taskUuid, file.getOriginalFilename());
                String versionId = amazonS3Driver.putObject(DEFAULT_REGION_ID, awsConfig, awsConfig.getSerDeploy().getBucketName(), keyName, inputStream, new ObjectMetadata());
                S3Object s3Object = amazonS3Driver.getObject(DEFAULT_REGION_ID, awsConfig, awsConfig.getSerDeploy().getBucketName(), keyName, versionId);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public DataTable<SerDeployVO.Task> queryProjectPage(SerDeployParam.TaskPageQuery pageQuery) {
        DataTable<SerDeployTask> table = serDeployTaskService.queryPageByParam(pageQuery);
        List<SerDeployVO.Task> data = BeanCopierUtil.copyListProperties(table.getData(), SerDeployVO.Task.class)
                .stream()
                .peek(e -> serDeployTaskPacker.wrap(e, pageQuery))
                .collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }
}
