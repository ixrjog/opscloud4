package com.baiyi.opscloud.tencent.exmail.driver;

import com.baiyi.opscloud.common.datasource.TencentExmailConfig;
import com.baiyi.opscloud.tencent.exmail.entity.ExmailToken;
import com.baiyi.opscloud.tencent.exmail.entity.ExmailUser;
import com.baiyi.opscloud.tencent.exmail.entity.base.BaseExmailResult;
import com.baiyi.opscloud.tencent.exmail.feign.TencentExmailUserFeign;
import com.baiyi.opscloud.tencent.exmail.param.ExmailParam;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/10/12 3:40 下午
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TencentExmailUserDriver {

    private final TencentExmailTokenDriver tencentExmailTokenDatasource;

    public static final long ALL_DEPARTMENT = 1L;

    private TencentExmailUserFeign buildFeign(TencentExmailConfig.Tencent config) {
        TencentExmailConfig.Exmail exmail = config.getExmail();
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(TencentExmailUserFeign.class, exmail.getApiUrl());
    }

    public void create(TencentExmailConfig.Tencent config, ExmailParam.User param) {
        ExmailToken exmailToken = tencentExmailTokenDatasource.getToken(config);
        TencentExmailUserFeign exmailAPI = buildFeign(config);
        BaseExmailResult result = exmailAPI.createUser(exmailToken.getAccessToken(), param);
    }

    public ExmailUser get(TencentExmailConfig.Tencent config, String userId) {
        ExmailToken exmailToken = tencentExmailTokenDatasource.getToken(config);
        TencentExmailUserFeign exmailAPI = buildFeign(config);
        return exmailAPI.getUser(exmailToken.getAccessToken(), userId);
    }

    public List<ExmailUser> list(TencentExmailConfig.Tencent config, Long departmentId) {
        ExmailToken exmailToken = tencentExmailTokenDatasource.getToken(config);
        TencentExmailUserFeign exmailAPI = buildFeign(config);
        return exmailAPI.listUser(exmailToken.getAccessToken(), departmentId);
    }

    public void update(TencentExmailConfig.Tencent config, ExmailParam.User param) {
        ExmailToken exmailToken = tencentExmailTokenDatasource.getToken(config);
        TencentExmailUserFeign exmailAPI = buildFeign(config);
        BaseExmailResult result = exmailAPI.updateUser(exmailToken.getAccessToken(), param);
    }

    public void delete(TencentExmailConfig.Tencent config, String userId) {
        ExmailToken exmailToken = tencentExmailTokenDatasource.getToken(config);
        TencentExmailUserFeign exmailAPI = buildFeign(config);
        BaseExmailResult result = exmailAPI.deleteUser(exmailToken.getAccessToken(), userId);
    }

}