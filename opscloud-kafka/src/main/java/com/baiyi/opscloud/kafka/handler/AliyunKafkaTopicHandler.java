package com.baiyi.opscloud.kafka.handler;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.baiyi.opscloud.domain.param.kafka.KafkaParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/14 5:44 下午
 * @Since 1.0
 */

@Slf4j
@Component
public class AliyunKafkaTopicHandler extends AliyunKafkaHandler {

    private interface ApiMethodName {
        String kafkaTopicCreate = "CreateTopic";
        String kafkaTopicQuery = "GetTopicStatus";
        String kafkaTopicModify = "ModifyPartitionNum";
    }

    public Boolean kafkaTopicCreate(KafkaParam.TopicCreate param) {
        IAcsClient iAcsClient = getIAcsClient(param.getInstanceName());
        CommonRequest request = getCommonRequest(param.getInstanceName());
        request.setSysMethod(MethodType.POST);
        request.setSysAction(ApiMethodName.kafkaTopicCreate);
        request.putQueryParameter("Topic", param.getTopic());
        request.putQueryParameter("Remark", param.getRemark());
        request.putQueryParameter("PartitionNum", String.valueOf(param.getPartitionNum()));
        try {
            iAcsClient.getCommonResponse(request);
            return true;
        } catch (ClientException e) {
            log.error("阿里云kafka创建Topic失败，Topic:{}", param.getTopic(), e);
            return false;
        }
    }

    public String kafkaTopicQuery(KafkaParam.TopicQuery param) {
        IAcsClient iAcsClient = getIAcsClient(param.getInstanceName());
        CommonRequest request = getCommonRequest(param.getInstanceName());
        request.setSysMethod(MethodType.POST);
        request.setSysAction(ApiMethodName.kafkaTopicQuery);
        request.putQueryParameter("Topic", param.getTopic());
        try {
            CommonResponse response = iAcsClient.getCommonResponse(request);
            return response.getData();
        } catch (ClientException e) {
            log.error("阿里云kafka查询Topic失败，Topic:{}", param.getTopic(), e);
            return Strings.EMPTY;
        }
    }

    public Boolean kafkaTopicModify(KafkaParam.TopicModify param) {
        IAcsClient iAcsClient = getIAcsClient(param.getInstanceName());
        CommonRequest request = getCommonRequest(param.getInstanceName());
        request.setSysMethod(MethodType.POST);
        request.setSysAction(ApiMethodName.kafkaTopicModify);
        request.putQueryParameter("Topic", param.getTopic());
        request.putQueryParameter("AddPartitionNum", String.valueOf(param.getAddPartitionNum()));
        try {
            iAcsClient.getCommonResponse(request);
            return true;
        } catch (ClientException e) {
            log.error("阿里云kafka修改Topic失败，Topic:{}", param.getTopic(), e);
            return false;
        }
    }


}
