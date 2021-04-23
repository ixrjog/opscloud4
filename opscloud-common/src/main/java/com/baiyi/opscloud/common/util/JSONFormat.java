package com.baiyi.opscloud.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Author baiyi
 * @Date 2020/5/19 10:43 上午
 * @Version 1.0
 */
public class JSONFormat {

    public static String format(Object obj) {
        return format(JSONUtils.writeValueAsString(obj));
    }

    /**
     * 格式化json
     *
     * @param jsonStr
     * @return
     */
    public static String format(String jsonStr) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object obj = mapper.readValue(jsonStr, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            return jsonStr;
        }
    }
}
