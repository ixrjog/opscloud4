package com.baiyi.opscloud.zabbix.mapper;

import com.baiyi.opscloud.zabbix.entry.ZabbixMacro;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/11/27 1:20 下午
 * @Version 1.0
 */
public class ZabbixMacroMapper implements ZabbixMapper<ZabbixMacro>{

    @Override
    public List<ZabbixMacro> mapFromJson(JsonNode jsonNode) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonNode.toString(), new TypeReference<List<ZabbixMacro>>() {
        });
    }
}
