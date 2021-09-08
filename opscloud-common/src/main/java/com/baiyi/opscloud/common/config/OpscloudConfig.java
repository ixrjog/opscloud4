package com.baiyi.opscloud.common.config;

import com.google.common.base.Joiner;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/9/8 1:41 下午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "opscloud", ignoreInvalidFields = true)
public class OpscloudConfig {

    public interface Paths {
        String ANSIBLE_PLAYBOOK = "ansible/playbook";
    }

    private String version;
    private String dataPath; // Opscloud数据目录

    /**
     * 获取Ansible playbook 路径
     *
     * @return
     */
    public String getAnsiblePlaybookPath() {
        return Joiner.on("/").join(dataPath, Paths.ANSIBLE_PLAYBOOK);
    }
}
