package com.baiyi.opscloud.zabbix.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

/**
 * @Author 修远
 * @Date 2021/6/25 3:50 下午
 * @Since 1.0
 */
public class ZabbixMapper {

    public static <T> List<T> mapperList(JsonNode jsonNode, Class<T> tClass) {
        List<T> list = Lists.newArrayList();
        Iterator<JsonNode> iterator = jsonNode.elements();
        while (iterator.hasNext()) {
            JsonNode data = iterator.next();
            list.add(mapper(data, tClass));
        }
        return list;
    }

    public static <T> T mapper(JsonNode jsonNode, Class<T> tClass) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonNode.toString(), tClass);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

}