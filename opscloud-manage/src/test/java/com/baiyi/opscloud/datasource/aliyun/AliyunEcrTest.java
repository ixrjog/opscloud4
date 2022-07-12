package com.baiyi.opscloud.datasource.aliyun;

import com.aliyuncs.cr.model.v20181201.ListRepositoryResponse;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.acr.driver.AliyunAcrRepositoryDriver;
import com.baiyi.opscloud.datasource.aliyun.base.BaseAliyunTest;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/7/12 11:31
 * @Version 1.0
 */
public class AliyunEcrTest extends BaseAliyunTest {

    @Resource
    private AliyunAcrRepositoryDriver aliyunAcrRepositoryDriver;

    private final static String instanceId = "cri-4v9b8l2gc3en0x34";

    @Test
    void listRepositoriesTest() {
        AliyunConfig config = getConfig();
        List<ListRepositoryResponse.RepositoriesItem> repositoriesItems = aliyunAcrRepositoryDriver.listRepositories("eu-west-1", config.getAliyun(), instanceId);
        print(repositoriesItems);
    }

}
