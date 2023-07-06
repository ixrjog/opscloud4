package com.baiyi.opscloud.datasource.aws;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.base.BaseAwsTest;
import com.baiyi.opscloud.datasource.aws.s3.driver.AmazonS3Driver;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @Author 修远
 * @Date 2023/6/5 2:16 PM
 * @Since 1.0
 */
public class S3Test extends BaseAwsTest {

    @Resource
    private AmazonS3Driver amazonS3Driver;

    @Test
    void getObjectTest() {
        AwsConfig.Aws awsConfig = getConfigById(23).getAws();
        S3Object object = amazonS3Driver.getObject(DEFAULT_REGION_ID, awsConfig, "s3-ser-deploy", "599079b5d6934d14b6f063eaaf1a2935/");
        print(object);
    }


    @Test
    void listObjectsTest() {
        AwsConfig.Aws awsConfig = getConfigById(23).getAws();
        List<S3ObjectSummary> objects =
                amazonS3Driver.listObjects(DEFAULT_REGION_ID, awsConfig, "s3-ser-deploy", "599079b5d6934d14b6f063eaaf1a2935/");
        print(objects);
    }
}
