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
 * @Date 2021/1/18 11:05 上午
 * @Since 1.0
 */

@Slf4j
@Component
public class AliyunKafkaGroupHandler extends AliyunKafkaHandler {
                                                
    private interface ApiMethodName {
        String kafkaGroupCreate = "CreateConsumerGroup";
        String kafkaGroupQuery = "GetConsumerProgress";
        String kafkaGroupListQuery = "GetConsumerList";
    }

    public Boolean kafkaGroupCreate(KafkaParam.GroupCreate param) {
        IAcsClient iAcsClient = getIAcsClient(param.getInstanceName());
        CommonRequest request = getCommonRequest(param.getInstanceName());
        request.setSysMethod(MethodType.POST);
        request.setSysAction(AliyunKafkaGroupHandler.ApiMethodName.kafkaGroupCreate);
        request.putQueryParameter("ConsumerId", param.getConsumerId());
        request.putQueryParameter("Remark", param.getRemark());
        try {
            iAcsClient.getCommonResponse(request);
            return true;
        } catch (ClientException e) {
            log.error("阿里云kafka创建ConsumerGroup失败，ConsumerGroup:{}", param.getConsumerId(), e);
            return false;
        }
    }

    public String kafkaGroupQuery(KafkaParam.GroupQuery param) {
        IAcsClient iAcsClient = getIAcsClient(param.getInstanceName());
        CommonRequest request = getCommonRequest(param.getInstanceName());
        request.setSysMethod(MethodType.POST);
        request.setSysAction(AliyunKafkaGroupHandler.ApiMethodName.kafkaGroupQuery);
        request.putQueryParameter("ConsumerId", param.getConsumerId());
        try {
            CommonResponse response = iAcsClient.getCommonResponse(request);
            return response.getData();
        } catch (ClientException e) {
            log.error("阿里云kafka查询ConsumerGroup失败，ConsumerGroup:{}", param.getConsumerId(), e);
            return Strings.EMPTY;
        }
    }

    public String kafkaGroupListQuery(String instanceName) {
        IAcsClient iAcsClient = getIAcsClient(instanceName);
        CommonRequest request = getCommonRequest(instanceName);
        request.setSysMethod(MethodType.POST);
        request.setSysAction(AliyunKafkaGroupHandler.ApiMethodName.kafkaGroupListQuery);
        try {
            CommonResponse response = iAcsClient.getCommonResponse(request);
            return response.getData();
        } catch (ClientException e) {
            log.error("阿里云kafka批量查询ConsumerGroup失败，instanceName:{}", instanceName, e);
            return Strings.EMPTY;
        }
    }

}
