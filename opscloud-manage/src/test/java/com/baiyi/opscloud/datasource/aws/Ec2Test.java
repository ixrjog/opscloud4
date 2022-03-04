package com.baiyi.opscloud.datasource.aws;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.provider.base.asset.SimpleAssetProvider;
import com.baiyi.opscloud.datasource.aws.base.BaseAwsTest;
import com.baiyi.opscloud.datasource.aws.ec2.drive.AmazonEc2Drive;
import com.baiyi.opscloud.datasource.aws.ec2.entity.Ec2Instance;
import com.baiyi.opscloud.datasource.aws.ec2.helper.AmazonEc2InstanceTypeHelper;
import com.baiyi.opscloud.datasource.aws.ec2.model.InstanceModel;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.net.MalformedURLException;
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
    private AmazonEc2Drive amazonEc2Drive;

    @Resource
    private AmazonEc2InstanceTypeHelper amazonEc2InstanceTypeHelper;

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
            URL url = new URL(getConfig().getAws().getEc2().getInstances());
            print("url = " + getConfig().getAws().getEc2().getInstances());
            print("host = " + url.getHost());
            print("path = " + url.getPath());
            print("protocol = " + url.getProtocol());
            print("port = " + url.getPort());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
