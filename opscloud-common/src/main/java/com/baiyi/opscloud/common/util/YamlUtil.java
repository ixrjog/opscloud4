package com.baiyi.opscloud.common.util;

import com.baiyi.opscloud.common.template.YamlVars;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

/**
 * @Author baiyi
 * @Date 2023/3/29 13:45
 * @Version 1.0
 */
public class YamlUtil {

    private YamlUtil() {
    }

    public static YamlVars.Vars loadVars(String vars) {
        if (StringUtils.isEmpty(vars)) {
            return YamlVars.Vars.EMPTY;
        }
        try {
            return loadAs(vars, YamlVars.Vars.class);
        } catch (Exception e) {
            return YamlVars.Vars.EMPTY;
        }
    }

    /**
     * 1.33
     * @param loadYaml
     * @param targetClass
     * @return
     * @param <T>
     * @throws JsonSyntaxException
     */
//    public static <T> T loadAs(String loadYaml, Class<T> targetClass) throws JsonSyntaxException {
//        Representer representer = new Representer(new DumperOptions());
//        representer.getPropertyUtils().setSkipMissingProperties(true);
//        LoaderOptions loaderOptions = new LoaderOptions();
//        Constructor constructor = new Constructor(targetClass, loaderOptions);
//        Yaml yaml = new Yaml(constructor, representer);
//        return yaml.loadAs(loadYaml, targetClass);
//    }

    /**
     * 2.0
     *
     * @param loadYaml
     * @param targetClass
     * @param <T>
     * @return
     * @throws JsonSyntaxException
     */
    public static <T> T loadAs(String loadYaml, Class<T> targetClass) throws JsonSyntaxException {
        Representer representer = new Representer(new DumperOptions());
        representer.getPropertyUtils().setSkipMissingProperties(true);
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setTagInspector(tag -> true);
        Constructor constructor = new Constructor(targetClass, loaderOptions);
        Yaml yaml = new Yaml(constructor, representer);
        return yaml.loadAs(loadYaml, targetClass);
    }

}
