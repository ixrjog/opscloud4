package com.baiyi.opscloud.leo.domain.model.base;

import com.google.gson.JsonSyntaxException;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * @Author baiyi
 * @Date 2022/11/11 10:19
 * @Version 1.0
 */
public abstract class YamlDump {

    public String dump() throws JsonSyntaxException {
        DumperOptions dumperOptions = new DumperOptions();
        // 设置层级显示
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        // 显示开始结束分隔符
        dumperOptions.setExplicitStart(true);
        dumperOptions.setExplicitEnd(true);
        // 缩进
        dumperOptions.setIndent(2);

        Yaml yaml = new Yaml(dumperOptions);
        return yaml.dump(this);
    }

}