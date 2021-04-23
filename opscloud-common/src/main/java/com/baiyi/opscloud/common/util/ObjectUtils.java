package com.baiyi.opscloud.common.util;

import com.google.common.collect.Maps;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/16 1:57 下午
 * @Since 1.0
 */
public class ObjectUtils {

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
