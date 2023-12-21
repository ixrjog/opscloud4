package com.baiyi.opscloud.common.util;

import com.baiyi.opscloud.domain.model.property.ServerProperty;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.stream.IntStream;

/**
 * @Author baiyi
 * @Date 2021/8/20 4:00 下午
 * @Version 1.0
 */
@Slf4j
public class BusinessPropertyUtil {

    public static <T> T toProperty(String property, Class<T> targetClass) throws JsonSyntaxException {
        return YamlUtil.loadAs(property, targetClass);
    }

    /**
     * 该方法是用于相同对象不同属性值的合并，如果两个相同对象中同一属性都有值，那么sourceBean中的值会覆盖targetBean重点的值
     *
     * @param sourceBean 被提取的对象bean
     * @param targetBean 用于合并的对象bean
     * @return targetBean, 合并后的对象
     */
    public static ServerProperty.Server combineServerProperty(ServerProperty.Server sourceBean, ServerProperty.Server targetBean) {
        Class<? extends ServerProperty.Server> sourceBeanClass = sourceBean.getClass();
        Class<? extends ServerProperty.Server> targetBeanClass = targetBean.getClass();

        Field[] sourceFields = sourceBeanClass.getDeclaredFields();
        Field[] targetFields = targetBeanClass.getDeclaredFields();
        IntStream.range(0, sourceFields.length).forEach(i -> {
            Field sourceField = sourceFields[i];
            if (Modifier.isStatic(sourceField.getModifiers())) {
                return;
            }
            Field targetField = targetFields[i];
            if (Modifier.isStatic(targetField.getModifiers())) {
                return;
            }
            sourceField.setAccessible(true);
            targetField.setAccessible(true);
            try {
                if (sourceField.get(sourceBean) != null && !"serialVersionUID".equals(sourceField.getName())) {
                    targetField.set(targetBean, sourceField.get(sourceBean));
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                log.error(e.getMessage());
            }
        });
        return targetBean;
    }

}