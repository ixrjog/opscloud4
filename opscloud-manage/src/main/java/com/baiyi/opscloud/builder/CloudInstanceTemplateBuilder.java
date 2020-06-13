package com.baiyi.opscloud.builder;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudInstanceTemplate;
import com.baiyi.opscloud.domain.vo.cloud.CloudInstanceTemplateVO;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

/**
 * @Author baiyi
 * @Date 2020/3/24 10:38 上午
 * @Version 1.0
 */
public class CloudInstanceTemplateBuilder {

    public static OcCloudInstanceTemplate build(CloudInstanceTemplateVO.InstanceTemplate instanceTemplate, Integer id) {
        OcCloudInstanceTemplate cit = covert(instanceTemplate);
        Yaml yaml = new Yaml();
        cit.setTemplateYaml(yaml.dumpAs(instanceTemplate, Tag.MAP, DumperOptions.FlowStyle.BLOCK));// 序列化对象为YAML-BLOCK格式);
        cit.setId(id);
        return cit;
    }


    private static OcCloudInstanceTemplate covert(CloudInstanceTemplateVO.InstanceTemplate instanceTemplate) {
        return BeanCopierUtils.copyProperties(instanceTemplate, OcCloudInstanceTemplate.class);
    }
}
