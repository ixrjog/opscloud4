package com.baiyi.opscloud.facade.datasource.instance;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.util.GitlabTokenUtil;
import com.baiyi.opscloud.domain.param.notify.gitlab.GitlabNotifyParam;
import com.baiyi.opscloud.factory.gitlab.GitlabEventNameEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author baiyi
 * @Date 2021/10/29 2:54 下午
 * @Version 1.0
 */
class GitlabFacadeTest extends BaseUnit implements InitializingBean {

    @Resource
    private GitlabFacade gitlabFacade;

    // 此Token并没有安全问题，仅用于匹配Gitlab实例
    private static final String token = "818e35c2952d716f29c831b728fa7818";

    @Test
    void consumeEventV4Test() {
        GitlabNotifyParam.SystemHook systemHook = GitlabNotifyParam.SystemHook.builder()
                .event_name(GitlabEventNameEnum.KEY_CREATE.name().toLowerCase())
                .user_name("baiyi")
                .id(999)
                .key("ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC58FwqHUbebw2SdT7SP4FxZ0w+lAO/erhy2ylhlcW/tZ3GY3mBu9VeeiSGoGz8hCx80Zrz+aQv28xfFfKlC8XQFpCWwsnWnQqO2Lv9bS8V1fIHgMxOHIt5Vs+9CAWGCCvUOAurjsUDoE2ALIXLDMKnJxcxD13XjWdK54j6ZXDB4syLF0C2PnAQSVY9X7MfCYwtuFmhQhKaBussAXpaVMRHltie3UYSBUUuZaB3J4cg/7TxlmxcNd+ppPRIpSZAB0NI6aOnqoBCpimscO/VpQRJMVLr3XiSYeT6HBiDXWHnIVPfQc03OGcaFqOit6p8lYKMaP/iUQLm+pgpZqrXZ9vB john@localhost")
                .build();
        gitlabFacade.consumeEventV4(systemHook);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        GitlabTokenUtil.setToken(token);
    }

}