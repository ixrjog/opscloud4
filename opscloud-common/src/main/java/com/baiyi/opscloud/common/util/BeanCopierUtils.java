package com.baiyi.opscloud.common.util;

import com.google.common.collect.MapMaker;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/1/7 1:52 下午
 * @Version 1.0
 */
public class BeanCopierUtils {

    private static Map<String, BeanCopier> beanCopierMap = new MapMaker().initialCapacity(32).concurrencyLevel(32).makeMap();

    /**
     * bean 对象copy
     *
     * @param source
     * @param targetClass
     */
    public static  <T> T copyProperties(Object source, Class<T> targetClass) {
        if(source == null){
            return null;
        }

        String beanKey = generateKey(source.getClass(), targetClass);
        BeanCopier copier;
        if (!beanCopierMap.containsKey(beanKey)) {
            copier = BeanCopier.create(source.getClass(), targetClass, false);
            beanCopierMap.put(beanKey, copier);
        } else {
            copier = beanCopierMap.get(beanKey);
        }

        T targetObject;
        try {
            targetObject = targetClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        copier.copy(source, targetObject, null);

        return targetObject;

    }

    /**
     * @param source
     * @param targetClass
     * @throws Exception
     */
    public static <T> List<T> copyListProperties(List<?> source, Class<T> targetClass) {

        if (CollectionUtils.isEmpty(source)) {
            return new ArrayList<>();
        }

        List<T> target = new ArrayList<T>();
        for (Object obj : source) {

            T targetObject = null;
            try {
                targetObject = targetClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            copyProperties(obj, targetObject);

            target.add(targetObject);
        }

        return target;
    }

    /**
     * bean 对象copy
     *
     * @param source
     * @param target
     */
    public static void copyProperties(Object source, Object target) {
        if(source == null){
            return;
        }
        String beanKey = generateKey(source.getClass(), target.getClass());
        BeanCopier copier;
        if (!beanCopierMap.containsKey(beanKey)) {
            copier = BeanCopier.create(source.getClass(), target.getClass(), false);
            beanCopierMap.put(beanKey, copier);
        } else {
            copier = beanCopierMap.get(beanKey);
        }
        copier.copy(source, target, null);

    }

    private static String generateKey(Class<?> class1, Class<?> class2) {
        return class1.toString() + class2.toString();
    }


}
