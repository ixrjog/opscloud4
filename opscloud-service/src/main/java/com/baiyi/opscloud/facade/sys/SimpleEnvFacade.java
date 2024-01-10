package com.baiyi.opscloud.facade.sys;

import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.service.sys.EnvService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/5/5 11:15
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class SimpleEnvFacade {

    private final EnvService envService;

    private static final String SYMBOL = "-";

    /**
     * Remove environment suffix 删除环境后缀
     */
    public String removeEnvSuffix(String str, String symbol) {
        List<Env> envs = envService.queryAllActive();
        for (Env env : envs) {
            if (str.endsWith(symbol + env.getEnvName())) {
                return str.replace(symbol + env.getEnvName(), "");
            }
        }
        return str;
    }

    public String removeEnvSuffix(String str) {
        return this.removeEnvSuffix(str, SYMBOL);
    }

}