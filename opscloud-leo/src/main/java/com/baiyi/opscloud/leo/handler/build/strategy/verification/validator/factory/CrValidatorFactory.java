package com.baiyi.opscloud.leo.handler.build.strategy.verification.validator.factory;

import com.baiyi.opscloud.leo.handler.build.strategy.verification.validator.base.BaseCrValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2022/11/18 14:55
 * @Version 1.0
 */
@SuppressWarnings("rawtypes")
@Slf4j
public class CrValidatorFactory {

    private CrValidatorFactory() {
    }

    static final Map<String, BaseCrValidator> CONTEXT = new ConcurrentHashMap<>();

    public static BaseCrValidator getValidatorByCrType(String crType) {
        return CONTEXT.get(crType);
    }

    public static void register(BaseCrValidator bean) {
        CONTEXT.put(bean.getCrType(), bean);
        log.debug("CrValidatorFactory Registered: crType={}", bean.getCrType());
    }

}