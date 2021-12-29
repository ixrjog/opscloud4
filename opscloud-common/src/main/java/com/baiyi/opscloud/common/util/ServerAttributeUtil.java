package com.baiyi.opscloud.common.util;

import com.baiyi.opscloud.common.config.serverAttribute.AttributeGroup;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.yaml.snakeyaml.Yaml;

/**
 * @Author baiyi
 * @Date 2020/3/7 10:42 上午
 * @Version 1.0
 */
public class ServerAttributeUtil {

    public static AttributeGroup convert(String attributeValue) {
        Yaml yaml = new Yaml();
        Object result = yaml.load(attributeValue);
        try {
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(JSONUtil.writeValueAsString(result), AttributeGroup.class);
        } catch (Exception e) {
            return new AttributeGroup();
        }
    }

}
