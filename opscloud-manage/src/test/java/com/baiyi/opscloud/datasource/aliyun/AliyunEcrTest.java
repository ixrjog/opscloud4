package com.baiyi.opscloud.datasource.aliyun;

import com.aliyuncs.cr.model.v20181201.*;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.acr.delegate.AliyunAcrInstanceDelegate;
import com.baiyi.opscloud.datasource.aliyun.acr.driver.AliyunAcrImageDriver;
import com.baiyi.opscloud.datasource.aliyun.acr.driver.AliyunAcrInstanceDriver;
import com.baiyi.opscloud.datasource.aliyun.acr.driver.AliyunAcrRepositoryDriver;
import com.baiyi.opscloud.datasource.aliyun.acr.entity.AliyunAcr;
import com.baiyi.opscloud.datasource.aliyun.base.BaseAliyunTest;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/7/12 11:31
 * @Version 1.0
 */
public class AliyunEcrTest extends BaseAliyunTest {

    @Resource
    private AliyunAcrRepositoryDriver aliyunAcrRepositoryDriver;

    @Resource
    private AliyunAcrInstanceDriver aliyunAcrInstanceDriver;

    @Resource
    private AliyunAcrInstanceDelegate aliyunAcrInstanceDelegate;

    @Resource
    private AliyunAcrImageDriver aliyunAcrImageDriver;

    private final static String instanceId = "cri-4v9b8l2gc3en0x34";

    @Test
    void listRepositoriesTest() {
        AliyunConfig config = getConfig();
        try {
            List<ListRepositoryResponse.RepositoriesItem> repositories = aliyunAcrRepositoryDriver.listRepository("eu-west-1", config.getAliyun(), instanceId);
            print(repositories);
        } catch (ClientException e) {
        }
    }


    /**
     * [
     * {
     * "modifiedTime":"1636945380000",
     * "instanceName":"cr-chuanyinet-eu-west-1",
     * "createTime":"1636945285000",
     * "instanceSpecification":"Enterprise_Basic",
     * "instanceStatus":"RUNNING",
     * "instanceId":"cri-4v9b8l2gc3en0x34",
     * "regionId":"eu-west-1"
     * }
     * ]
     */
    @Test
    void listInstanceTest() {
        AliyunConfig config = getConfig();
        try {
            List<ListInstanceResponse.InstancesItem> instancesItems = aliyunAcrInstanceDriver.listInstance("eu-west-1", config.getAliyun());
            print(instancesItems);
        } catch (ClientException e) {

        }
    }

    @Test
    void getInstanceEndpointTest() {
        AliyunConfig config = getConfig();
        try {
            List<GetInstanceEndpointResponse.Endpoints> endpoints = aliyunAcrInstanceDriver.getInstanceEndpoint("eu-west-1", config.getAliyun(), "cri-4v9b8l2gc3en0x34");
            print(endpoints);
        } catch (Exception e) {
        }
    }

    @Test
    void listInstanceTest2() {
        AliyunConfig config = getConfig();
        try {
            List<AliyunAcr.Instance> instances = aliyunAcrInstanceDelegate.listInstance("eu-west-1", config.getAliyun());
            print(instances);
        } catch (ClientException e) {
        }
    }

    @Test
    void listNamespaceTest() {
        AliyunConfig config = getConfig();
        try {
            List<ListNamespaceResponse.NamespacesItem> namespacesItems = aliyunAcrInstanceDriver.listNamespace("eu-west-1", config.getAliyun(), "cri-4v9b8l2gc3en0x34");
            print(namespacesItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void listNamespaceTest2() {
        AliyunConfig config = getConfig();
        try {
            List<AliyunAcr.Namespace> namespaces = aliyunAcrInstanceDelegate.listNamespace("eu-west-1", config.getAliyun(), "cri-4v9b8l2gc3en0x34");
            print(namespaces);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void listImageTest() {
        AliyunConfig config = getConfig();
        try {
            List<ListRepoTagResponse.ImagesItem> imagesItems = aliyunAcrImageDriver.listImage("eu-west-1", config.getAliyun(), "cri-4v9b8l2gc3en0x34", "crr-zla5udc2kw7ly0fk");
            print(imagesItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void listImage2Test() {
        AliyunConfig config = getConfig();
        try {
            List<ListRepoTagResponse.ImagesItem> imagesItems = aliyunAcrImageDriver.listImage("eu-west-1", config.getAliyun(), "cri-4v9b8l2gc3en0x34", "crr-zla5udc2kw7ly0fk", 3);
            for (ListRepoTagResponse.ImagesItem imagesItem : imagesItems) {
                print(imagesItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getRepositoryIdTest() {
        AliyunConfig config = getConfig();
        try {
            String repoId = aliyunAcrRepositoryDriver.getRepositoryId("eu-west-1", config.getAliyun(), "cri-4v9b8l2gc3en0x34", "gray", "mgw-core-aliyun");
            print(repoId);

             repoId = aliyunAcrRepositoryDriver.getRepositoryId("eu-central-1", config.getAliyun(), "cri-koab4h4dfgxosahl", "daily", "data-message-frankfurt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
