package com.baiyi.opscloud.leo.action.build.concrete.post.verify.factory;

import com.baiyi.opscloud.leo.action.build.concrete.post.verify.base.BaseCrImageValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2022/11/18 14:55
 * @Version 1.0
 */
@Slf4j
public class CrImageValidatorFactory {

    private CrImageValidatorFactory() {
    }

    static Map<String, BaseCrImageValidator> context = new ConcurrentHashMap<>();

    public static BaseCrImageValidator getValidatorByCrType(String crType) {
        return context.get(crType);
    }

    public static void register(BaseCrImageValidator bean) {
        context.put(bean.getCrType(), bean);
        log.info("CrImageValidatorFactory Registered: crType={}", bean.getCrType());
    }

}
