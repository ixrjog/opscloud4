package com.baiyi.opscloud.cloud.vpc.factory;

import com.baiyi.opscloud.cloud.vpc.ICloudVPC;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2020/1/9 3:42 下午
 * @Version 1.0
 */
public class CloudVPCFactory {

    static Map<String, ICloudVPC> context = new ConcurrentHashMap<>();

    public static ICloudVPC getCloudVPCByKey(String key) {
        return context.get(key);
    }

    public static void register(ICloudVPC bean) {
        context.put(bean.getKey(), bean);
    }

    public static Map<String, ICloudVPC> getCloudVPCContainer() {
        return context;
    }


}
