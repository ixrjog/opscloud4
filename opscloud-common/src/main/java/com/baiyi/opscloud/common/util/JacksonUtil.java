package com.baiyi.opscloud.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * @Author 修远
 * @Date 2022/5/23 1:22 PM
 * @Since 1.0
 */
public class JacksonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * JSON字符串反序列化成Java对象
     *
     * @param json
     * @return
     */
    public static <T> T parse(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
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
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return StringUtils.EMPTY;
    }


}
