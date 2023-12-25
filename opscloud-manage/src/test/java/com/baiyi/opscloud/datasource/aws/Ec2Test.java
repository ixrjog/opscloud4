package com.baiyi.opscloud.datasource.aws;

import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Tag;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.provider.base.asset.SimpleAssetProvider;
import com.baiyi.opscloud.datasource.aws.base.BaseAwsTest;
import com.baiyi.opscloud.datasource.aws.ec2.driver.AmazonEc2Driver;
import com.baiyi.opscloud.datasource.aws.ec2.entity.Ec2Instance;
import com.baiyi.opscloud.datasource.aws.ec2.helper.AmazonEc2InstanceTypeHelper;
import com.baiyi.opscloud.datasource.aws.ec2.model.InstanceModel;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/1/21 11:15 AM
 * @Version 1.0
 */
public class Ec2Test extends BaseAwsTest {

    @Resource
    private AmazonEc2Driver amazonEc2Drive;

    @Resource
    private AmazonEc2InstanceTypeHelper amazonEc2InstanceTypeHelper;

    private interface regionId {
        String HK = "ap-east-1";
        String Ireland = "eu-west-1";
    }

    @Test
    void pullAssetTest() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.AWS.getName(), DsAssetTypeConstants.EC2.name());
        assert assetProvider != null;
        assetProvider.pullAsset(20);
    }


    @Test
    void getAmazonEc2InstanceTypeMapTest() {
        try {
            Map<String, InstanceModel.EC2InstanceType> map = amazonEc2InstanceTypeHelper.getAmazonEc2InstanceTypeMap(getConfig().getAws());
            print(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void listInstancesTest() {
        List<Ec2Instance.Instance> instances = amazonEc2Drive.listInstances("eu-west-2", getConfig().getAws());
        print(instances);
    }

    @Test
    void urlTest() {
        try {
            URL url = URI.create(getConfig().getAws().getEc2().getInstances()).toURL();
            print("url = " + getConfig().getAws().getEc2().getInstances());
            print("host = " + url.getHost());
            print("path = " + url.getPath());
            print("protocol = " + url.getProtocol());
            print("port = " + url.getPort());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void listByInstanceIdsTest() {
        List<String> instanceIds = Lists.newArrayList(
                "i-0578b3b0c351f98a4", "i-0415d0f0a965aeab2"
        );
        List<Instance> instances = amazonEc2Drive.listByInstanceIds(getConfig().getAws(), regionId.Ireland, instanceIds);
        List<String> resources = Lists.newArrayList();
        instances.forEach(e -> {
            resources.add(e.getInstanceId());
            e.getBlockDeviceMappings().forEach(ebs -> resources.add(ebs.getEbs().getVolumeId()));
        });
        Tag tag = new Tag("FIN", "CN");
        List<Tag> tagList = Lists.newArrayList(tag);
        amazonEc2Drive.createTags(getConfig().getAws(), regionId.Ireland, resources, tagList);
    }

}