package com.baiyi.opscloud.common.util;

import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import com.baiyi.opscloud.common.exception.datasource.DatasourceException;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.StringUtils;

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
     *
     * @param propsYml
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T extends BaseDsConfig> T toDsConfig(String propsYml, Class<T> targetClass) {
        if (StringUtils.isEmpty(propsYml)) {
            throw new DatasourceException(ErrorEnum.DATASOURCE_PROPS_EMPTY);
        }
        try {
            return YamlUtil.loadAs(propsYml, targetClass);
        } catch (JsonSyntaxException e) {
            throw new DatasourceException(ErrorEnum.DATASOURCE_PROPS_CONVERT_ERROR);
        }
    }

}