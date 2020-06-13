package com.baiyi.opscloud.zabbix.mapper;

import com.baiyi.opscloud.zabbix.entry.ZabbixHostgroup;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/8 10:58 上午
 * @Version 1.0
 */
public class ZabbixHostgroupMapper implements ZabbixMapper<ZabbixHostgroup> {

    @Override
    public List<ZabbixHostgroup> mapFromJson(JsonNode jsonNode) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonNode.toString(), new TypeReference<List<ZabbixHostgroup>>() {
        });
    }
}
