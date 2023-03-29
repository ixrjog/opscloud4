package com.baiyi.opscloud.common.util;

import com.google.gson.JsonSyntaxException;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.inspector.TrustedTagInspector;
import org.yaml.snakeyaml.representer.Representer;

/**
 * @Author baiyi
 * @Date 2023/3/29 13:45
 * @Version 1.0
 */
public class YamlUtil {

    private YamlUtil() {
    }

    public static <T> T loadAs(String loadYaml, Class<T> targetClass) throws JsonSyntaxException {
        Representer representer = new Representer(new DumperOptions());
        representer.getPropertyUtils().setSkipMissingProperties(true);
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setTagInspector(new TrustedTagInspector());
        Constructor constructor = new Constructor(targetClass, loaderOptions);
        Yaml yaml = new Yaml(constructor, representer);
        return yaml.loadAs(loadYaml, targetClass);
    }

}
