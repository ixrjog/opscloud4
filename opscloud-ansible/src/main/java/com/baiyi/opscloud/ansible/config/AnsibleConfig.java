package com.baiyi.opscloud.ansible.config;

import com.google.common.base.Joiner;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/4/6 5:28 下午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "ansible", ignoreInvalidFields = true)
public class AnsibleConfig {

    public final static String ANSIBLE_HOSTS = "ansible_hosts";

    private String bin;
    private String version;
    private String playbookBin;
    private String logPath;
    //    主机配置文件目录
    private String dataPath;

    public String acqInventoryPath(){
        return Joiner.on("/").join(dataPath,"inventory");
    }

    public String acqScriptPath(){
        return Joiner.on("/").join(dataPath,"script");
    }

    public String acqPlaybookPath(){
        return Joiner.on("/").join(dataPath,"playbook");
    }

    public String acqPrivateKey(){
        return Joiner.on("/").join(dataPath,"private_key/id_rsa");
    }

}
