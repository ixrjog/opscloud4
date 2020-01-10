package com.baiyi.opscloud.service;

import com.baiyi.opscloud.domain.generator.OcEnv;

/**
 * @Author baiyi
 * @Date 2020/1/10 2:16 下午
 * @Version 1.0
 */
public interface OcEnvService {

    OcEnv queryOcEnvById(Integer id);
    OcEnv queryOcEnvByName(String name);
}
