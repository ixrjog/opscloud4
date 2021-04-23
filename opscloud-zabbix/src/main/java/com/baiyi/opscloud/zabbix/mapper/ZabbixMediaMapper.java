package com.baiyi.opscloud.zabbix.mapper;

import com.baiyi.opscloud.zabbix.entry.ZabbixMedia;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/12/9 3:19 下午
 * @Version 1.0
 */
public class ZabbixMediaMapper implements ZabbixMapper<ZabbixMedia> {

    @Override
    public List<ZabbixMedia> mapFromJson(JsonNode jsonNode) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonNode.toString(),new TypeReference<List<ZabbixMedia>>() {});
    }


}
