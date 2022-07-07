package com.baiyi.opscloud.datasource.huaweicloud.ecs.driver;

import com.baiyi.opscloud.common.datasource.HuaweicloudConfig;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.core.http.HttpConfig;
import com.huaweicloud.sdk.ecs.v2.EcsClient;
import com.huaweicloud.sdk.ecs.v2.model.ListServersDetailsRequest;
import com.huaweicloud.sdk.ecs.v2.model.ListServersDetailsResponse;
import com.huaweicloud.sdk.ecs.v2.region.EcsRegion;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author baiyi
 * @Date 2022/7/7 14:29
 * @Version 1.0
 */
@Slf4j
public class HuaweicloudEcsDriver {

    public static void listServers(String regionId,HuaweicloudConfig.Huaweicloud huaweicloud) {
        try {
            // 实例化ListVpcsRequest请求对象，调用listVpcs接口
            EcsClient client = buildEcsClient(regionId,huaweicloud);

            ListServersDetailsRequest request = new ListServersDetailsRequest();
            request.setLimit(100);

            ListServersDetailsResponse listServersDetailsResponse = client.listServersDetails(request );

            log.info(listServersDetailsResponse.toString());
        } catch (ServiceResponseException e) {
            log.error("HttpStatusCode: " + e.getHttpStatusCode());
            log.error("RequestId: " + e.getRequestId());
            log.error("ErrorCode: " + e.getErrorCode());
            log.error("ErrorMsg: " + e.getErrorMsg());
        }
    }

    // HuaweicloudConfig
    public static EcsClient buildEcsClient(String regionId, HuaweicloudConfig.Huaweicloud huaweicloud) {
        // 配置客户端属性
        HttpConfig config = HttpConfig.getDefaultHttpConfig();
        config.withIgnoreSSLVerification(true);

        // 创建认证
        BasicCredentials auth = new BasicCredentials()
                .withAk(huaweicloud.getAccount().getAccessKeyId())
                .withSk(huaweicloud.getAccount().getSecretAccessKey());

        return EcsClient.newBuilder()
                .withHttpConfig(config)
                .withCredential(auth)
                .withRegion(EcsRegion.valueOf(regionId))
                .build();

    }
}
