package com.baiyi.opscloud.datasource.aws.ec2.helper;

import com.baiyi.opscloud.common.configuration.CachingConfiguration;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.ec2.feign.AmazonEc2InstanceFeign;
import com.baiyi.opscloud.datasource.aws.ec2.model.InstanceModel;
import com.google.common.base.Joiner;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/1/21 11:42 AM
 * @Version 1.0
 */
@Component
public class AmazonEc2InstanceTypeHelper {

    private AmazonEc2InstanceFeign buildFeign(String url) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(AmazonEc2InstanceFeign.class, url);
    }

    /**
     * 查询EC2实例规格
     *
     * @param config
     * @return
     * @throws Exception
     */
    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1W, key = "'amazon_ec2_instances_details'")
    public Map<String, InstanceModel.EC2InstanceType> getAmazonEc2InstanceTypeMap(AwsConfig.Aws config) throws Exception {
        URL url = config.getEc2().toURL();
        AmazonEc2InstanceFeign awsEc2API = buildFeign(Joiner.on("://").join(url.getProtocol(), url.getHost()));
        return awsEc2API.getInstances(url.getPath());
    }

}