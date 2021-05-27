package com.baiyi.caesar.common.datasource;

import lombok.Data;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/5/15 6:40 下午
 * @Version 1.0
 */
@Data
public class BaseDsInstanceConfig {

    private String name;

    private String url;

    private Map<String, String> props;

}
