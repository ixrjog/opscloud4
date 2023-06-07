package com.baiyi.opscloud.datasource.aws;

import com.amazonaws.services.s3.model.S3Object;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.base.BaseAwsTest;
import com.baiyi.opscloud.datasource.aws.s3.driver.AmazonS3Driver;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

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
        S3Object object = amazonS3Driver.getObject(DEFAULT_REGION_ID, awsConfig,"transsnet-applogs-new","jvm-log/1218.txt");
        print(object);
    }
}
