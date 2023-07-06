package com.baiyi.opscloud.datasource.aws.s3.driver;

import com.amazonaws.services.s3.model.*;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.s3.service.AmazonS3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @Author 修远
 * @Date 2023/5/31 6:03 PM
 * @Since 1.0
 */

@Slf4j
@Component
public class AmazonS3Driver {

    //https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/java/example_code/s3/src/main/java/aws/example/s3/PutObject.java

    /**
     * @return String versionId
     */
    public String putObject(String regionId, AwsConfig.Aws config, String bucketName, String keyName, File file) {
        PutObjectResult result = AmazonS3Service.buildAmazonS3(regionId, config)
                .putObject(bucketName, keyName, file);
        return result.getVersionId();
    }

    public String putObject(String regionId, AwsConfig.Aws config, String bucketName, String keyName, InputStream inputStream, ObjectMetadata metadata) {
        PutObjectResult result = AmazonS3Service.buildAmazonS3(regionId, config)
                .putObject(bucketName, keyName, inputStream, metadata);
        return result.getVersionId();
    }

    public S3Object getObject(String regionId, AwsConfig.Aws config, String bucketName, String keyName) {
        return AmazonS3Service.buildAmazonS3(regionId, config)
                .getObject(bucketName, keyName);
    }

    public S3Object getObject(String regionId, AwsConfig.Aws config, String bucketName, String key, String versionId) {
        GetObjectRequest request = new GetObjectRequest(bucketName, key, versionId);
        return AmazonS3Service.buildAmazonS3(regionId, config)
                .getObject(request);
    }

    public void deleteObject(String regionId, AwsConfig.Aws config, String bucketName, String key) {
        DeleteObjectRequest request = new DeleteObjectRequest(bucketName, key);
        AmazonS3Service.buildAmazonS3(regionId, config)
                .deleteObject(request);
    }

    public List<S3ObjectSummary> listObjects(String regionId, AwsConfig.Aws config, String bucketName, String prefix) {
        return AmazonS3Service.buildAmazonS3(regionId, config)
                .listObjects(bucketName, prefix)
                .getObjectSummaries();
    }

}
