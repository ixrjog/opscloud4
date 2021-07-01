package com.baiyi.opscloud.common.util;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.common.exception.datasource.DatasourceRuntimeException;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

/**
 * @Author baiyi
 * @Date 2020/7/3 4:29 下午
 * @Version 1.0
 */
public class DsUtil {

    // TemplateVariable
    public static <T> T toDatasourceConfig(String propsYml, Class<T> targetClass) {
        if (StringUtils.isEmpty(propsYml))
            throw new DatasourceRuntimeException(ErrorEnum.DATASOURCE_PROPS_EMPTY);
        try {
            Yaml yaml = new Yaml();
            Object result = yaml.load(propsYml);
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(JSON.toJSONString(result), targetClass);
        } catch (JsonSyntaxException e) {
            throw new DatasourceRuntimeException(ErrorEnum.DATASOURCE_PROPS_CONVERT_ERROR);
        }
    }



}
