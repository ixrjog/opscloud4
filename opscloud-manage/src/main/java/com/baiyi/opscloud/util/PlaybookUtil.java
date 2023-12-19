package com.baiyi.opscloud.util;

import com.baiyi.opscloud.common.configuration.properties.OpscloudConfigurationProperties;
import com.baiyi.opscloud.domain.generator.opscloud.AnsiblePlaybook;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/9/22 10:34 上午
 * @Version 1.0
 */
@Component
public class PlaybookUtil {

    private PlaybookUtil() {
    }

    private static OpscloudConfigurationProperties opscloudConfigurationProperties;

    @Autowired
    public void setOpscloudConfig(OpscloudConfigurationProperties opscloudConfig) {
        PlaybookUtil.opscloudConfigurationProperties = opscloudConfig;
    }

    public static String toPath(AnsiblePlaybook ansiblePlaybook) {
        String fileName = Joiner.on(".").join(ansiblePlaybook.getPlaybookUuid(), "yml");
        return Joiner.on("/").join(opscloudConfigurationProperties.getAnsiblePlaybookPath(), fileName);
    }

}
