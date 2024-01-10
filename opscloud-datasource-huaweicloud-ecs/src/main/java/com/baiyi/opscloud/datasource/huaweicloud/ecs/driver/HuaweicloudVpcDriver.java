package com.baiyi.opscloud.datasource.huaweicloud.ecs.driver;


import com.baiyi.opscloud.common.datasource.HuaweicloudConfig;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.core.http.HttpConfig;
import com.huaweicloud.sdk.core.region.Region;
import com.huaweicloud.sdk.vpc.v2.region.VpcRegion;
import com.huaweicloud.sdk.vpc.v3.VpcClient;
import com.huaweicloud.sdk.vpc.v3.model.ListVpcsRequest;
import com.huaweicloud.sdk.vpc.v3.model.ListVpcsResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author baiyi
 * @Date 2022/7/7 14:34
 * @Version 1.0
 */
@Slf4j
public class HuaweicloudVpcDriver {

    public static void listVpcs(HuaweicloudConfig.Huaweicloud huaweicloud) {
        try {
            // 实例化ListVpcsRequest请求对象，调用listVpcs接口
            VpcClient client = buildVpcClient(huaweicloud);
            ListVpcsResponse listVpcsResponse = client.listVpcs(new ListVpcsRequest().withLimit(1));
            // 输出json格式的字符串响应
            log.info(listVpcsResponse.toString());
        } catch (ServiceResponseException e) {
            log.error("HttpStatusCode: " + e.getHttpStatusCode());
            log.error("RequestId: " + e.getRequestId());
            log.error("ErrorCode: " + e.getErrorCode());
            log.error("ErrorMsg: " + e.getErrorMsg());
        }
    }

    // HuaweicloudConfig
    public static VpcClient buildVpcClient(HuaweicloudConfig.Huaweicloud huaweicloud) {

        //  String endpoint = "{your endpoint string}";
        //  String projectId = "{your project id}";

        // 配置客户端属性
        HttpConfig config = HttpConfig.getDefaultHttpConfig();
        config.withIgnoreSSLVerification(true);

        // 创建认证
        BasicCredentials auth = new BasicCredentials()
                .withAk(huaweicloud.getAccount().getAccessKeyId())
                .withSk(huaweicloud.getAccount().getSecretAccessKey());
        //  .withProjectId(projectId);
        // ap-southeast-1
        Region region = VpcRegion.valueOf("ap-southeast-1");

        // 创建VpcClient实例并初始化
        return VpcClient.newBuilder()
                .withHttpConfig(config)
                .withCredential(auth)
                // .withEndpoint("ap-southeast-1")
                .withRegion(region)
                .build();

    }

}