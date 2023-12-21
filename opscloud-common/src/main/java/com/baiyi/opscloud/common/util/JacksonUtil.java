package com.baiyi.opscloud.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * @Author 修远
 * @Date 2022/5/23 1:22 PM
 * @Since 1.0
 */
@Slf4j
public class JacksonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * JSON字符串反序列化成Java对象
     *
     * @param json
     * @return
     */
    public static <T> T parse(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    /**
     * Java对象序列化成JSON字符串
     *
     * @param object
     * @return
     */
    public static String toJSONString(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        return StringUtils.EMPTY;
    }

}