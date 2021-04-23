package com.baiyi.opscloud.zabbix.mapper;

import com.baiyi.opscloud.zabbix.entry.ZabbixTrigger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/2/18 10:30 上午
 * @Version 1.0
 */
public class ZabbixTriggerMapper implements ZabbixMapper<ZabbixTrigger> {

    @Override
    public List<ZabbixTrigger> mapFromJson(JsonNode jsonNode) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonNode.toString(), new TypeReference<List<ZabbixTrigger>>() {
        });
    }

}
