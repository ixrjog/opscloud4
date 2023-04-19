package com.baiyi.opscloud.datasource.facade;

import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.datasource.facade.am.base.IAccessManagementProcessor;
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

    private static final Map<String, IAccessManagementProcessor> CONTEXT = new ConcurrentHashMap<>();

    public static void register(IAccessManagementProcessor bean) {
        CONTEXT.put(bean.getDsType(), bean);
        log.debug("AM Processor Registered: dsType={}, beanName={}", bean.getDsType(), bean.getClass().getSimpleName());
    }

    /**
     * 授权策略
     *
     * @param grantPolicy
     */
    public void grantPolicy(UserAmParam.GrantPolicy grantPolicy) {
        IAccessManagementProcessor xamProcessor = getAmProcessorByInstanceUuid(grantPolicy.getInstanceUuid());
        xamProcessor.grantPolicy(grantPolicy);
    }

    /**
     * 撤销已授权策略
     *
     * @param revokePolicy
     */
    public void revokePolicy(UserAmParam.RevokePolicy revokePolicy) {
        IAccessManagementProcessor xamProcessor = getAmProcessorByInstanceUuid(revokePolicy.getInstanceUuid());
        xamProcessor.revokePolicy(revokePolicy);
    }

    public void createUser(UserAmParam.CreateUser createUser) {
        IAccessManagementProcessor xamProcessor = getAmProcessorByInstanceUuid(createUser.getInstanceUuid());
        xamProcessor.createUser(createUser);
    }

    private IAccessManagementProcessor getAmProcessorByInstanceUuid(String uuid) {
        DatasourceInstance instance = dsInstanceService.getByUuid(uuid);
        if (instance == null) {
            throw new OCException("数据源实例不存在！");
        }
        if (!CONTEXT.containsKey(instance.getInstanceType())) {
            throw new OCException("数据源实例类型不正确！");
        }
        return CONTEXT.get(instance.getInstanceType());
    }

    public void updateLoginProfile(UserAmParam.UpdateLoginProfile updateLoginProfile){
        IAccessManagementProcessor xamProcessor = getAmProcessorByInstanceUuid(updateLoginProfile.getInstanceUuid());
        xamProcessor.updateLoginProfile(updateLoginProfile);
    }

}
