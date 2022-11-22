package com.baiyi.opscloud.common.util;

import com.baiyi.opscloud.common.exception.datasource.DatasourceRuntimeException;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

/**
 * @Author baiyi
 * @Date 2020/7/3 4:29 下午
 * @Version 1.0
 */
public class DsUtil {

    private DsUtil() {
    }

    /**
     * https://stackabuse.com/reading-and-writing-yaml-files-in-java-with-snakeyaml/
     * @param propsYml
     * @param targetClass
     * @return
     * @param <T>
     */
    public static <T> T toDsConfig(String propsYml, Class<T> targetClass) {
        if (StringUtils.isEmpty(propsYml))
            throw new DatasourceRuntimeException(ErrorEnum.DATASOURCE_PROPS_EMPTY);
        try {
            Representer representer = new Representer(new DumperOptions());
            representer.getPropertyUtils().setSkipMissingProperties(true);
            Yaml yaml = new Yaml(new Constructor(targetClass),representer);
            return yaml.loadAs(propsYml, targetClass);
        } catch (JsonSyntaxException e) {
            throw new DatasourceRuntimeException(ErrorEnum.DATASOURCE_PROPS_CONVERT_ERROR);
        }
    }

}
