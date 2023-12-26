package com.baiyi.opscloud.datasource.dingtalk.driver;

import com.baiyi.opscloud.common.configuration.CachingConfiguration;
import com.baiyi.opscloud.common.datasource.DingtalkConfig;
import com.baiyi.opscloud.datasource.dingtalk.entity.DingtalkDepartment;
import com.baiyi.opscloud.datasource.dingtalk.entity.DingtalkToken;
import com.baiyi.opscloud.datasource.dingtalk.feign.DingtalkDepartmentFeign;
import com.baiyi.opscloud.datasource.dingtalk.param.DingtalkDepartmentParam;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/11/29 6:08 下午
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DingtalkDepartmentDriver {

    private final DingtalkTokenDriver dingtalkTokenDriver;

    private DingtalkDepartmentFeign buildFeign(DingtalkConfig.Dingtalk config) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(DingtalkDepartmentFeign.class, config.getUrl());
    }

    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1H, key = "'corp_id_'+ #config.corpId + '_dingtalk_list_sub_id_by_dept_id_' + #listSubDepartmentId.deptId", unless = "#result == null")
    public DingtalkDepartment.DepartmentSubIdResponse listSubId(DingtalkConfig.Dingtalk config, DingtalkDepartmentParam.ListSubDepartmentId listSubDepartmentId) {
        DingtalkToken.TokenResponse tokenResponse = dingtalkTokenDriver.getToken(config);
        DingtalkDepartmentFeign dingtalkAPI = buildFeign(config);
        log.debug("未命中缓存: method=listSubId, deptId={}", listSubDepartmentId.getDeptId());
        return dingtalkAPI.listSubId(tokenResponse.getAccessToken(), listSubDepartmentId);
    }

    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1H, key = "'corp_id_'+ #config.corpId + '_dingtalk_get_dept_id_' + #getDepartment.deptId", unless = "#result == null")
    public DingtalkDepartment.GetDepartmentResponse get(DingtalkConfig.Dingtalk config, DingtalkDepartmentParam.GetDepartment getDepartment) {
        DingtalkToken.TokenResponse tokenResponse = dingtalkTokenDriver.getToken(config);
        DingtalkDepartmentFeign dingtalkAPI = buildFeign(config);
        log.debug("未命中缓存: method=get, deptId={}", getDepartment.getDeptId());
        return dingtalkAPI.get(tokenResponse.getAccessToken(), getDepartment);
    }

}