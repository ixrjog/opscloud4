package com.baiyi.opscloud.zabbix.mapper;


import com.baiyi.opscloud.zabbix.entry.ZabbixUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;


public class ZabbixUserMapper implements ZabbixMapper<ZabbixUser> {

    @Override
    public List<ZabbixUser> mapFromJson(JsonNode jsonNode) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonNode.toString(),new TypeReference<List<ZabbixUser>>() {});
    }

}
