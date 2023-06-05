package com.baiyi.opscloud.datasource.aws.s3.driver;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.s3.service.AmazonS3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @Author 修远
 * @Date 2023/5/31 6:03 PM
 * @Since 1.0
 */

@Slf4j
@Component
public class AmazonS3Driver {

    //https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/java/example_code/s3/src/main/java/aws/example/s3/PutObject.java
    public void putObject(String regionId, AwsConfig.Aws config, String bucketName, String keyName, File file) {
        PutObjectResult result = AmazonS3Service.buildAmazonS3(regionId, config)
                .putObject(bucketName, keyName, file);
        log.error(result.getContentMd5());
    }
}
