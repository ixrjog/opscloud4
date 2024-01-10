package com.baiyi.opscloud.datasource.aliyun.dms.client;

import com.aliyun.teaopenapi.models.Config;
import com.baiyi.opscloud.common.datasource.AliyunConfig;

import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2021/12/16 2:31 PM
 * @Version 1.0
 */
public class AliyunDmsClient {

    public static com.aliyun.dms_enterprise20181101.Client createClient(AliyunConfig.Aliyun aliyun) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(aliyun.getAccount().getAccessKeyId())
                // 您的AccessKey Secret
                .setAccessKeySecret(aliyun.getAccount().getSecret());
        // 访问的域名
        config.endpoint = Optional.of(aliyun)
                .map(AliyunConfig.Aliyun::getDms)
                .map(AliyunConfig.Dms::getEndpoint)
                .orElse(AliyunConfig.DMS_ENDPOINT);
        return new com.aliyun.dms_enterprise20181101.Client(config);
    }

    public static DmsClient createMyClient(AliyunConfig.Aliyun aliyun) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(aliyun.getAccount().getAccessKeyId())
                // 您的AccessKey Secret
                .setAccessKeySecret(aliyun.getAccount().getSecret());
        // 访问的域名
        config.endpoint = Optional.of(aliyun)
                .map(AliyunConfig.Aliyun::getDms)
                .map(AliyunConfig.Dms::getEndpoint)
                .orElse(AliyunConfig.DMS_ENDPOINT);
        return new DmsClient(config);
    }

}