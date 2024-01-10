package com.baiyi.opscloud.datasource.aliyun.ons.client;

import com.aliyun.rocketmq20220801.Client;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.StringFormatter;

/**
 * @Author 修远
 * @Date 2023/9/11 4:31 PM
 * @Since 1.0
 */
public class AliyunOnsV5Client {

    private static final String ENDPOINT = "rocketmq.{}.aliyuncs.com";

    public static Client buildClient(String regionId, AliyunConfig.Aliyun aliyun) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(aliyun.getAccount().getAccessKeyId())
                .setAccessKeySecret(aliyun.getAccount().getSecret());
        // Endpoint 请参考 https://api.aliyun.com/product/RocketMQ
        config.endpoint = StringFormatter.format(ENDPOINT, regionId);
        return new Client(config);
    }

}