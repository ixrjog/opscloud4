package com.baiyi.opscloud.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.List;

/**
 * @Author 修远
 * @Date 2021/10/12 5:53 下午
 * @Since 1.0
 */
@Slf4j
public class JsonNodeMapperUtil {

    private static <T> List<T> mapperList(JsonNode jsonNode, Class<T> tClass) {
        List<T> list = Lists.newArrayList();
        Iterator<JsonNode> iterator = jsonNode.elements();
        while (iterator.hasNext()) {
            JsonNode data = iterator.next();
            list.add(mapper(data, tClass));
        }
        return list;
    }

    private static <T> T mapper(JsonNode jsonNode, Class<T> tClass) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonNode.toString(), tClass);
        } catch (JsonProcessingException e) {
            log.debug(e.getMessage());
            return null;
        }
    }

}