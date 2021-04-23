package com.baiyi.opscloud.kafka.helper;

import com.baiyi.opscloud.kafka.config.KafkaConfig;
import com.google.common.collect.Maps;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/13 4:03 下午
 * @Since 1.0
 */

@EnableKafka
@Component
public class KafkaClientHelper implements InitializingBean {

    private static Map<String, AdminClient> adminClientMap = Maps.newHashMap();

    private static Map<String, KafkaConfig.KafkaInstance> instanceMap = Maps.newHashMap();

    @Resource
    private KafkaConfig kafkaConfig;

    private void initClientMap() {
        List<KafkaConfig.KafkaInstance> instanceList = kafkaConfig.getInstances();
        instanceList.forEach(instance -> {
            instanceMap.put(instance.getInstanceName(), instance);
            if (instance.getInstanceType().equals(1)) {
                AdminClient adminClient = adminClient(kafkaAdmin(instance.getInstanceId()));
                adminClientMap.put(instance.getInstanceName(), adminClient);
            }
        });
    }

    private KafkaAdmin kafkaAdmin(String clusterNodes) {
        Map<String, Object> props = new HashMap<>();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, clusterNodes);
        return new KafkaAdmin(props);
    }

    private AdminClient adminClient(KafkaAdmin kafkaAdmin) {
        return AdminClient.create(kafkaAdmin.getConfig());
    }

    public AdminClient getKafkaAdminClient(String instanceName) {
        return adminClientMap.get(instanceName);
    }

    public KafkaConfig.KafkaInstance getKafkaInstance(String instanceName) {
        return instanceMap.get(instanceName);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initClientMap();
    }
}
