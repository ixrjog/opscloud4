package com.baiyi.opscloud.datasource.huaweicloud.ecs.driver;

import com.baiyi.opscloud.common.datasource.HuaweicloudConfig;
import com.baiyi.opscloud.datasource.huaweicloud.ecs.entity.HuaweicloudEcs;
import com.google.common.collect.Lists;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.core.http.HttpConfig;
import com.huaweicloud.sdk.ecs.v2.EcsClient;
import com.huaweicloud.sdk.ecs.v2.model.ListServersDetailsRequest;
import com.huaweicloud.sdk.ecs.v2.model.ListServersDetailsResponse;
import com.huaweicloud.sdk.ecs.v2.model.ServerAddress;
import com.huaweicloud.sdk.ecs.v2.model.ServerDetail;
import com.huaweicloud.sdk.ecs.v2.region.EcsRegion;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/7/7 14:29
 * @Version 1.0
 */
@Slf4j
public class HuaweicloudEcsDriver {

    public interface Query {
        int LIMIT = 100;
    }

    public static List<HuaweicloudEcs.Ecs> listServers(String regionId, HuaweicloudConfig.Huaweicloud huaweicloud) {
        List<ServerDetail> serverDetails = Lists.newArrayList();
        EcsClient client = buildEcsClient(regionId, huaweicloud);
        ListServersDetailsRequest request = new ListServersDetailsRequest();
        request.setLimit(Query.LIMIT);
        int size = Query.LIMIT;
        int pageNo = 1;
        try {
            while (Query.LIMIT <= size) {
                ListServersDetailsResponse response = client.listServersDetails(request);
                serverDetails.addAll(response.getServers());
                size = response.getServers().size();
                pageNo++;
            }
        } catch (ServiceResponseException e) {
            log.error("查询错误: httpStatusCode={}, requestId={}, errorCode={}, errorMsg={}", e.getHttpStatusCode(), e.getRequestId(), e.getErrorCode(), e.getErrorMsg());
        }
        return serverDetails.stream().map(e-> toEcs(regionId,e)).collect(Collectors.toList());
    }

    public static HuaweicloudEcs.Ecs toEcs(String regionId,ServerDetail serverDetail) {
        Map<String, List<ServerAddress>> addMap = serverDetail.getAddresses();
        String privateIp = "";
        String publicIp = "";
        for (String key : addMap.keySet()) {
            for (ServerAddress serverAdd : addMap.get(key)) {
                if(ServerAddress.OsEXTIPSTypeEnum.FIXED == serverAdd.getOsEXTIPSType()){
                    privateIp = serverAdd.getAddr();
                    continue;
                }
                if(ServerAddress.OsEXTIPSTypeEnum.FLOATING ==serverAdd.getOsEXTIPSType()){
                    publicIp = serverAdd.getAddr();
                }
            }
        }
        return HuaweicloudEcs.Ecs.builder()
                .regionId(regionId)
                .id(serverDetail.getId())
                .name(serverDetail.getName())
                .status(serverDetail.getStatus())
                .kind(serverDetail.getFlavor().getName())
                .zone(serverDetail.getOsEXTAZAvailabilityZone())
                .created(serverDetail.getCreated())
                .disk(serverDetail.getFlavor().getDisk())
                .vcpus(serverDetail.getFlavor().getVcpus())
                .ram(serverDetail.getFlavor().getRam())
                .osType(serverDetail.getMetadata().get("os_type"))
                .privateIp(privateIp)
                .publicIp(publicIp)
                .build();
    }

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