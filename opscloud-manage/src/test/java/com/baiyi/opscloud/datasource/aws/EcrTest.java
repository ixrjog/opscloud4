package com.baiyi.opscloud.datasource.aws;

import com.amazonaws.services.ecr.model.ImageDetail;
import com.amazonaws.services.ecr.model.Repository;
import com.baiyi.opscloud.datasource.aws.base.BaseAwsTest;
import com.baiyi.opscloud.datasource.aws.ecr.driver.AmazonEcrImageDriver;
import com.baiyi.opscloud.datasource.aws.ecr.driver.AmazonEcrRepositoryDirver;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/8/16 17:10
 * @Version 1.0
 */
public class EcrTest extends BaseAwsTest {

    @Resource
    private AmazonEcrRepositoryDirver amazonEcrRepositoryDirver;

    @Resource
    private AmazonEcrImageDriver amazonEcrImageDriver;

    @Test
    void describeRepositoriesTest() {
        // m-workflow
        Repository repository = amazonEcrRepositoryDirver.describeRepository("eu-west-1", getConfig().getAws(), "m-workflow");
        //      print(repository);
        List<ImageDetail> imageDetails = amazonEcrImageDriver.describeImages("eu-west-1", getConfig().getAws(), "502076313352", "m-workflow");
        print(imageDetails);
    }

}
