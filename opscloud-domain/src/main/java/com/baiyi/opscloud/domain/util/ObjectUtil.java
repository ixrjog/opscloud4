package com.baiyi.opscloud.domain.util;

import com.google.common.collect.Maps;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/12/1 7:18 下午
 * @Version 1.0
 */
public class ObjectUtil {

    private ObjectUtil() {}

    /**
     * 对象转Map
     * @param obj
     * @return
     */
    public static Map<String, Object> objectToMap(Object obj) {
        if (obj != null) {
            try {
                Map<String, Object> map = Maps.newHashMap();
                Field[] declaredFields = obj.getClass().getDeclaredFields();
                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    map.put(field.getName(), field.get(obj));
                }
                return map;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return Collections.emptyMap();
    }

}
