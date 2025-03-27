package com.baiyi.opscloud.datasource.aliyun;

import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.base.BaseAliyunTest;
import com.baiyi.opscloud.datasource.aliyun.ons.driver.*;
import com.baiyi.opscloud.datasource.aliyun.ons.entity.OnsGroupV5;
import com.baiyi.opscloud.datasource.aliyun.ons.entity.OnsInstanceV5;
import com.baiyi.opscloud.datasource.aliyun.ons.entity.OnsTopicV5;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @Author 修远
 * @Date 2023/9/11 10:36 PM
 * @Since 1.0
 */
public class AliyunOnsTest extends BaseAliyunTest {

    private static final String DEV_INSTANCE_ID = "rmq-cn-uqm3b3xou0d";

    @Resource
    private AliyunOnsInstanceV5Driver aliyunOnsInstanceV5Driver;

    @Resource
    private AliyunOnsTopicV5Driver aliyunOnsTopicV5Driver;

    @Resource
    private AliyunOnsGroupV5Driver aliyunOnsGroupV5Driver;

    @Resource
    private AliyunOnsRocketMqTopicDriver aliyunOnsRocketMqTopicDriver;

    @Resource
    private AliyunOnsRocketMqGroupDriver aliyunOnsRocketMqGroupDriver;


    @Test
    void listInstanceTest() throws Exception {
        AliyunConfig config = getConfig();
        List<OnsInstanceV5.InstanceInfo> list = aliyunOnsInstanceV5Driver.listInstance("eu-central-1",
                config.getAliyun());
        print(list);
    }

    @Test
    void listTopicTest() throws Exception {
        AliyunConfig config = getConfig();
        List<OnsTopicV5.Topic> list = aliyunOnsTopicV5Driver.listTopic("eu-central-1", config.getAliyun(),
                DEV_INSTANCE_ID);
        print(list);
    }

    @Test
    void listGroupTest() throws Exception {
        AliyunConfig config = getConfig();
        List<OnsGroupV5.Group> list = aliyunOnsGroupV5Driver.listGroup("eu-central-1", config.getAliyun(),
                DEV_INSTANCE_ID);
        print(list);
    }


    @Test
    void deleteGroupTest() throws Exception {
        String instanceId = "MQ_INST_1859120988191686_BYlwPOXl";
        AliyunConfig config = getConfig();
        aliyunOnsRocketMqGroupDriver.deleteGroup("eu-central-1", config.getAliyun(),
                instanceId, "GID_NG_OPAY_CHANNEL");
    }

    @Test
    void deleteTopicTest() throws Exception {
        String instanceId = "MQ_INST_1859120988191686_BYlwPOXl";
        AliyunConfig config = getConfig();
        aliyunOnsRocketMqTopicDriver.deleteTopic("eu-central-1", config.getAliyun(),
                instanceId, "TOPIC_FLEXI_LLM_OUTPUT_NOTIFY");
    }


    @Test
    void batchDeleteTopicTest() throws Exception {
        String instanceId = "MQ_INST_1859120988191686_BYLi12y8";
        AliyunConfig config = getConfig();
        aliyunOnsRocketMqTopicDriver.listTopic("eu-central-1", config.getAliyun(), instanceId)
                .forEach(e -> {
                    try {
                        aliyunOnsRocketMqTopicDriver.deleteTopic("eu-central-1", config.getAliyun(), instanceId,
                                e.getTopic());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
    }


    @Test
    void batchDeleteGroupTest() throws Exception {
        String instanceId = "MQ_INST_1859120988191686_BYLi12y8";
        AliyunConfig config = getConfig();
        aliyunOnsRocketMqGroupDriver.listGroup("eu-central-1", config.getAliyun(), instanceId)
                .forEach(e -> {
                    try {
                        aliyunOnsRocketMqGroupDriver.deleteGroup("eu-central-1", config.getAliyun(), instanceId,
                                e.getGroupId());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
    }
}
