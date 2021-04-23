package com.baiyi.opscloud.service.aliyun.ons;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsTopic;
import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/9 5:37 下午
 * @Since 1.0
 */
public interface OcAliyunOnsTopicService {

    void addOcAliyunOnsTopic(OcAliyunOnsTopic ocAliyunOnsTopic);

    void updateOcAliyunOnsTopic(OcAliyunOnsTopic ocAliyunOnsTopic);

    void deleteOcAliyunOnsTopicById(int id);

    DataTable<OcAliyunOnsTopic> queryOcAliyunOnsTopicByInstanceId(AliyunONSParam.TopicPageQuery pageQuery);

    List<OcAliyunOnsTopic> queryOcAliyunOnsTopicByInstanceId(String instanceId);

    OcAliyunOnsTopic queryOcAliyunOnsTopicByInstanceIdAndTopic(String instanceId, String topic);

    int countOcAliyunOnsTopicByInstanceId(String instanceId);

    int countOcAliyunOnsTopic();

    List<OcAliyunOnsTopic> queryOcAliyunOnsTopicByTopic(String topic);
}
