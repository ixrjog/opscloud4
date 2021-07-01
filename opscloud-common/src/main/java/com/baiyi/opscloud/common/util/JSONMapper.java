package com.baiyi.opscloud.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @Author baiyi
 * @Date 2020/1/2 10:31 上午
 * @Version 1.0
 */
public class JSONMapper extends ObjectMapper {
    public JSONMapper() {
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }
}