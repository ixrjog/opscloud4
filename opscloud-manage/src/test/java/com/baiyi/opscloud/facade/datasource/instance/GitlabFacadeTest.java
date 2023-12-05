package com.baiyi.opscloud.facade.datasource.instance;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.param.notify.gitlab.GitLabNotifyParam;
import com.baiyi.opscloud.factory.gitlab.enums.GitLabEventNameEnum;
import jakarta.annotation.Resource;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2021/10/29 2:54 下午
 * @Version 1.0
 */
class GitlabFacadeTest extends BaseUnit {

    @Resource
    private GitLabFacade gitlabFacade;

    // 此Token并没有安全问题，仅用于匹配Gitlab实例
    private static final String token = "818e35c2952d716f29c831b728fa7818";

    @Test
    void consumeEventV4Test() {
        GitLabNotifyParam.SystemHook systemHook = GitLabNotifyParam.SystemHook.builder()
                .event_name(GitLabEventNameEnum.KEY_CREATE.name().toLowerCase())
                .user_name("baiyi")
                .id(999)
                .key("ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC58FwqHUbebw2SdT7SP4FxZ0w+lAO/erhy2ylhlcW/tZ3GY3mBu9VeeiSGoGz8hCx80Zrz+aQv28xfFfKlC8XQFpCWwsnWnQqO2Lv9bS8V1fIHgMxOHIt5Vs+9CAWGCCvUOAurjsUDoE2ALIXLDMKnJxcxD13XjWdK54j6ZXDB4syLF0C2PnAQSVY9X7MfCYwtuFmhQhKaBussAXpaVMRHltie3UYSBUUuZaB3J4cg/7TxlmxcNd+ppPRIpSZAB0NI6aOnqoBCpimscO/VpQRJMVLr3XiSYeT6HBiDXWHnIVPfQc03OGcaFqOit6p8lYKMaP/iUQLm+pgpZqrXZ9vB john@localhost")
                .build();
        gitlabFacade.consumeEventV4(systemHook, "x");
    }


    @Test
    void consumeEventV4Test2() {

        GitLabNotifyParam.Project project = GitLabNotifyParam.Project.builder()
                .id(434)
                .name("Ant Design Pro")
                .web_url("http://gitlab.chuanyinet.com/ops/ant-design-pro")
                .git_ssh_url("git@gitlab.chuanyinet.com:ops/ant-design-pro.git")
                .git_http_url("http://gitlab.chuanyinet.com/ops/ant-design-pro.git")
                .namespace("ops")
                .visibility_level(0)
                .path_with_namespace("ops/ant-design-pro")
                .default_branch("master")
                .homepage("http://gitlab.chuanyinet.com/ops/ant-design-pro")
                .url("git@gitlab.chuanyinet.com:ops/ant-design-pro.git")
                .ssh_url("git@gitlab.chuanyinet.com:ops/ant-design-pro.git")
                .http_url("http://gitlab.chuanyinet.com/ops/ant-design-pro.git")
                .build();

        GitLabNotifyParam.SystemHook systemHook = GitLabNotifyParam.SystemHook.builder()
                .event_name(GitLabEventNameEnum.PUSH.name().toLowerCase())
                .user_id(2)
                .user_name("白衣")
                .user_email("jian.liang@palmpay-inc.com")
                .project_id(434)
                .refs(Lists.newArrayList("refs/heads/dev"))
                .project(project)
                .build();
        gitlabFacade.consumeEventV4(systemHook, "x");
    }

}