package com.baiyi.opscloud.common.configuration.properties;

import com.google.common.base.Joiner;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/9/8 1:41 下午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "opscloud", ignoreInvalidFields = true)
public class OpscloudConfigurationProperties {

    /**
     * createUser:
     * roles:
     * - base
     * userGroups:
     * - vpn-users
     * - confluence-users
     */
    @Data
    public static class CreateUser {
        private List<String> roles;
        private List<String> userGroups;
    }

    /**
     * outapi:
     * workorder:
     * approval
     */
    @Data
    public static class Outapi {
        private Workorder workorder;
    }

    @Data
    public static class Workorder {
        private String approval;
    }

    public interface Paths {
        String ANSIBLE_PLAYBOOK = "ansible/playbook";
        String SERVER_TASK_LOG = "logs/serverTask";
    }

    private String version;

    /**
     * Opscloud数据目录
     */
    private String dataPath;
    private CreateUser createUser;
    private Outapi outapi;

    /**
     * 获取Ansible playbook 路径
     *
     * @return
     */
    public String getAnsiblePlaybookPath() {
        return Joiner.on("/").join(dataPath, Paths.ANSIBLE_PLAYBOOK);
    }

    /**
     * 获取服务器任务日志目录
     *
     * @return
     */
    public String getServerTaskLogPath() {
        return Joiner.on("/").join(dataPath, Paths.SERVER_TASK_LOG);
    }

}
