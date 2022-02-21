package com.baiyi.opscloud.datasource.facade;

import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.datasource.facade.am.base.IXamProcessor;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.param.user.UserAmParam;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2022/2/10 6:26 PM
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserAmFacade {

    private final DsInstanceService dsInstanceService;

    private static final Map<String, IXamProcessor> context = new ConcurrentHashMap<>();

    public static void register(IXamProcessor bean) {
        context.put(bean.getDsType(), bean);
        log.info("AM Processor注册: dsType = {} , beanName = {}  ", bean.getDsType(), bean.getClass().getSimpleName());
    }

    /**
     * 授权策略
     * @param grantPolicy
     */
    public void grantPolicy(UserAmParam.GrantPolicy grantPolicy) {
        IXamProcessor xamProcessor = getAmProcessorByInstanceUuid(grantPolicy.getInstanceUuid());
        xamProcessor.grantPolicy(grantPolicy);
    }

    /**
     * 撤销已授权策略
     * @param revokePolicy
     */
    public void revokePolicy(UserAmParam.RevokePolicy revokePolicy) {
        IXamProcessor xamProcessor = getAmProcessorByInstanceUuid(revokePolicy.getInstanceUuid());
        xamProcessor.revokePolicy(revokePolicy);
    }

    public void createUser(UserAmParam.CreateUser createUser) {
        IXamProcessor xamProcessor = getAmProcessorByInstanceUuid(createUser.getInstanceUuid());
        xamProcessor.createUser(createUser);
    }

    private IXamProcessor getAmProcessorByInstanceUuid(String uuid) {
        DatasourceInstance instance = dsInstanceService.getByUuid(uuid);
        if (instance == null) {
            throw new CommonRuntimeException("数据源实例不存在！");
        }
        if (!context.containsKey(instance.getInstanceType())) {
            throw new CommonRuntimeException("数据源实例类型不正确！");
        }
        return context.get(instance.getInstanceType());
    }

}
