package com.baiyi.opscloud.dubbo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/10/9 1:48 下午
 * @Version 1.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "zookeeper", ignoreInvalidFields = true)
public class ZookeeperConfig {

    private List<ZookeeperCluster> clusters;

    @Data
    public static class ZookeeperCluster {

        // 环境名称
        private String env;
        // 集群成员访问地址
        private String address;
    }

}
