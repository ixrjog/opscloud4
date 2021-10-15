package com.baiyi.opscloud.tencent.exmail.handler;

import com.baiyi.opscloud.common.datasource.config.DsTencentExmailConfig;
import com.baiyi.opscloud.tencent.exmail.entry.ExmailToken;
import com.baiyi.opscloud.tencent.exmail.entry.ExmailUser;
import com.baiyi.opscloud.tencent.exmail.entry.base.BaseExmailModel;
import com.baiyi.opscloud.tencent.exmail.feign.TencentExmailUserFeign;
import com.baiyi.opscloud.tencent.exmail.param.ExmailParam;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/10/12 3:40 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class TencentExmailUserHandler {

    @Resource
    private TencentExmailTokenHandler tencentExmailTokenHandler;

    public static final long ALL_DEPARTMENT = 1L;

    private TencentExmailUserFeign buildFeign(DsTencentExmailConfig.Tencent config) {
        DsTencentExmailConfig.Exmail exmail = config.getExmail();
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(TencentExmailUserFeign.class, exmail.getApiUrl());
    }

    public void create(DsTencentExmailConfig.Tencent config, ExmailParam.User param) {
        ExmailToken exmailToken = tencentExmailTokenHandler.getToken(config);
        TencentExmailUserFeign exmailAPI = buildFeign(config);
        BaseExmailModel result = exmailAPI.createUser(exmailToken.getAccessToken(), param);
    }

    public ExmailUser get(DsTencentExmailConfig.Tencent config, String userId) {
        ExmailToken exmailToken = tencentExmailTokenHandler.getToken(config);
        TencentExmailUserFeign exmailAPI = buildFeign(config);
        return exmailAPI.getUser(exmailToken.getAccessToken(), userId);
    }

    public List<ExmailUser> list(DsTencentExmailConfig.Tencent config, Long departmentId) {
        ExmailToken exmailToken = tencentExmailTokenHandler.getToken(config);
        TencentExmailUserFeign exmailAPI = buildFeign(config);
        return exmailAPI.listUser(exmailToken.getAccessToken(), departmentId);
    }

    public void update(DsTencentExmailConfig.Tencent config, ExmailParam.User param) {
        ExmailToken exmailToken = tencentExmailTokenHandler.getToken(config);
        TencentExmailUserFeign exmailAPI = buildFeign(config);
        BaseExmailModel result = exmailAPI.updateUser(exmailToken.getAccessToken(), param);
    }

    public void delete(DsTencentExmailConfig.Tencent config, String userId) {
        ExmailToken exmailToken = tencentExmailTokenHandler.getToken(config);
        TencentExmailUserFeign exmailAPI = buildFeign(config);
        BaseExmailModel result = exmailAPI.deleteUser(exmailToken.getAccessToken(), userId);
    }
}
