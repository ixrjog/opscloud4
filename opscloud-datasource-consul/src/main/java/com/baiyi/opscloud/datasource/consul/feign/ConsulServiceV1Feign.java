package com.baiyi.opscloud.datasource.consul.feign;

import com.baiyi.opscloud.datasource.consul.entity.ConsulHealth;
import com.baiyi.opscloud.datasource.consul.entity.ConsulService;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/7/14 13:45
 * @Version 1.0
 */
public interface ConsulServiceV1Feign {

    /**
     * https://consul-prod.transspay.net/v1/internal/ui/services?dc=dc1
     *
     * @param dataCenter
     * @return
     */
    @RequestLine("GET /v1/internal/ui/services?dc={dataCenter}")
    @Headers({"Content-Type: application/json"})
    List<ConsulService.Service> listServices(@Param("dataCenter") String dataCenter);


    /**
     * https://consul-prod.transspay.net/v1/health/service/account?dc=dc1
     */
    @RequestLine("GET /v1/health/service/{service}?dc={dataCenter}")
    @Headers({"Content-Type: application/json"})
    List<ConsulHealth.Health> listHealthService(@Param("service") String service,
                                    @Param("dataCenter") String dataCenter);

}
