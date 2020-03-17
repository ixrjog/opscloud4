package com.baiyi.opscloud.cloud.image.factory;

import com.baiyi.opscloud.cloud.image.ICloudImage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/1/9 3:42 下午
 * @Version 1.0
 */
public class CloudImageFactory {

    static Map<String, ICloudImage> context = new ConcurrentHashMap<>();

    public static ICloudImage getCloudImageByKey(String key) {
        return context.get(key);
    }

    public static void register(ICloudImage bean) {
        context.put(bean.getKey(), bean);
    }

    public static Map<String, ICloudImage> getCloudImageContainer() {
        return context;
    }


}
