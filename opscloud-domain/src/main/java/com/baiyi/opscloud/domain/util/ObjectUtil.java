package com.baiyi.opscloud.domain.util;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/12/1 7:18 下午
 * @Version 1.0
 */
@Slf4j
public class ObjectUtil {

    private ObjectUtil() {
    }

    /**
     * 对象转Map
     *
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
                log.error(e.getMessage());
            }
        }
        return Collections.emptyMap();
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if ((obj instanceof List)) {
            return ((List<?>) obj).isEmpty();
        }
        if ((obj instanceof String)) {
            return ((String) obj).trim().isEmpty();
        }
        return false;
    }

    /**
     * 判断对象不为空
     *
     * @param obj 对象名
     * @return 是否不为空
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

}