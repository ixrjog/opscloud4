package com.baiyi.opscloud.datasource.aws;

import com.amazonaws.services.ec2.model.Instance;
import com.baiyi.opscloud.datasource.aws.base.BaseAwsTest;
import com.baiyi.opscloud.datasource.aws.ec2.drive.AmazonEc2Drive;
import com.baiyi.opscloud.datasource.aws.ec2.helper.AmazonEc2InstanceTypeHelper;
import com.baiyi.opscloud.datasource.aws.ec2.model.InstanceModel;
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
    void getAmazonEc2InstanceTypeMapTest(){
        try {
            Map<String, InstanceModel.EC2InstanceType> map = amazonEc2InstanceTypeHelper.getAmazonEc2InstanceTypeMap(getConfig().getAws());
            print(map);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void listInstancesTest() {
        List<Instance> instances = amazonEc2Drive.listInstances(getConfig().getAws());
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
