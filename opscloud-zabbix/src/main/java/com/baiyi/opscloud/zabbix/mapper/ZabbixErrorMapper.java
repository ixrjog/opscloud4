package com.baiyi.opscloud.zabbix.mapper;

import com.baiyi.opscloud.zabbix.entry.ZabbixError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Author baiyi
 * @Date 2020/12/3 3:51 下午
 * @Version 1.0
 */
public class ZabbixErrorMapper {

    public ZabbixError mapFromJson(JsonNode jsonNode) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonNode.toString(), new TypeReference<ZabbixError>() {});
    }
}