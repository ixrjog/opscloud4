package com.baiyi.opscloud.leo.action.build.concrete.post.validator.factory;

import com.baiyi.opscloud.leo.action.build.concrete.post.validator.base.BaseCrValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2022/11/18 14:55
 * @Version 1.0
 */
@Slf4j
public class CrValidatorFactory {

    private CrValidatorFactory() {
    }

    static Map<String, BaseCrValidator> context = new ConcurrentHashMap<>();

    public static BaseCrValidator getValidatorByCrType(String crType) {
        return context.get(crType);
    }

    public static void register(BaseCrValidator bean) {
        context.put(bean.getCrType(), bean);
        log.info("CrValidatorFactory Registered: crType={}", bean.getCrType());
    }

}
