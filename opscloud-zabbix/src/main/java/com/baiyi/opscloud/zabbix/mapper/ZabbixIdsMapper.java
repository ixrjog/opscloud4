package com.baiyi.opscloud.zabbix.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/8 11:37 上午
 * @Version 1.0
 */
public class ZabbixIdsMapper implements ZabbixMapper<String> {

    @Override
    public List<String> mapFromJson(JsonNode jsonNode) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonNode.toString(), new TypeReference<List<String>>() {
        });
    }
}
