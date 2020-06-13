package com.baiyi.opscloud.common.util;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.common.config.serverAttribute.AttributeGroup;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.yaml.snakeyaml.Yaml;

/**
 * @Author baiyi
 * @Date 2020/3/7 10:42 上午
 * @Version 1.0
 */
public class ServerAttributeUtils {

    public static AttributeGroup convert(String attributeValue) {
        Yaml yaml = new Yaml();
        Object result = yaml.load(attributeValue);
        try {
            Gson gson = new GsonBuilder().create();
            AttributeGroup ag = gson.fromJson(JSON.toJSONString(result), AttributeGroup.class);
            return ag;
        } catch (Exception e) {
            return new AttributeGroup();
        }
    }

}
