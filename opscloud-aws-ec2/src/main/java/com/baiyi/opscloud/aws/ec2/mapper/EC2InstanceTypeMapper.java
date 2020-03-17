package com.baiyi.opscloud.aws.ec2.mapper;

import com.baiyi.opscloud.aws.ec2.base.EC2InstanceType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/1/8 10:58 上午
 * @Version 1.0
 */
public class EC2InstanceTypeMapper {

    public Map<String, EC2InstanceType> mapFromJson(JsonNode jsonNode) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonNode.toString(), new TypeReference<Map<String, EC2InstanceType>>() {
        });
    }
}
