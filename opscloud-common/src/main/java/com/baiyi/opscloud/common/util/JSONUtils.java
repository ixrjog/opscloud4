package com.baiyi.opscloud.common.util;


import com.fasterxml.jackson.databind.JsonNode;

/**
 * @Author baiyi
 * @Date 2020/1/2 10:32 上午
 * @Version 1.0
 */
public class JSONUtils {

    private JSONMapper mapper = new JSONMapper();


    public static String writeValueAsString(Object object) {
        try {
            JSONMapper mapper = new JSONMapper();
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            return "";
        }
    }

    public static JsonNode readTree(String data) {
        try {
            JSONMapper mapper = new JSONMapper();
            return mapper.readTree(data);
        } catch (Exception e) {
            return null;
        }
    }

    public static JsonNode readTree(byte[] data) {
        try {
            JSONMapper mapper = new JSONMapper();
            return mapper.readTree(new String(data));
        } catch (Exception e) {
            return null;
        }
    }


    public static <T> T readValue(String content, Class<T> valueType) {
        try {
            JSONMapper mapper = new JSONMapper();
            return mapper.readValue(content, valueType);
        } catch (Exception e) {
            return null;
        }
    }

}
