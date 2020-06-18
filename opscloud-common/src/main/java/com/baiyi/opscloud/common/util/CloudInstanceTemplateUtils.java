package com.baiyi.opscloud.common.util;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.domain.vo.cloud.CloudInstanceTemplateVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.yaml.snakeyaml.Yaml;

/**
 * @Author baiyi
 * @Date 2020/3/24 9:37 上午
 * @Version 1.0
 */
public class CloudInstanceTemplateUtils {

    public static CloudInstanceTemplateVO.InstanceTemplate convert(String templateYAML) {
        Yaml yaml = new Yaml();
        Object result = yaml.load(templateYAML);
        try {
            Gson gson = new GsonBuilder().create();
            CloudInstanceTemplateVO.InstanceTemplate it = gson.fromJson(JSON.toJSONString(result), CloudInstanceTemplateVO.InstanceTemplate.class);
            return it;
        } catch (Exception e) {
            return  new CloudInstanceTemplateVO.InstanceTemplate();
        }
    }
}
