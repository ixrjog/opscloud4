package com.baiyi.opscloud.datasource.aws;

import com.amazonaws.services.ecr.model.Repository;
import com.baiyi.opscloud.datasource.aws.base.BaseAwsTest;
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

    @Test
    void describeRepositoriesTest() {
        List<Repository> repositories = amazonEcrRepositoryDirver.describeRepositories("eu-west-1", getConfig().getAws());
        print(repositories);
    }

}
