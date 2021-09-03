package com.baiyi.opscloud.facade.sys.impl;

import com.baiyi.opscloud.common.util.HostUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Instance;
import com.baiyi.opscloud.domain.vo.sys.InstanceVO;
import com.baiyi.opscloud.facade.sys.InstanceFacade;
import com.baiyi.opscloud.service.sys.InstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author baiyi
 * @Date 2021/9/3 1:31 下午
 * @Version 1.0
 */
@Slf4j
@Service
public class InstanceFacadeImpl implements InstanceFacade, InitializingBean {

    public interface healthStatus {
        String OK = "OK";
        String ERROR = "ERROR";
        String INACTIVE = "INACTIVE";
    }

    @Resource
    private InstanceService instanceService;

    @Override
    public boolean isHealth() {
        InstanceVO.Health health = checkHealth();
        return healthStatus.OK.equals(health.getStatus());
    }

    @Override
    public InstanceVO.Health checkHealth() {
        try {
            InetAddress inetAddress = HostUtil.getInetAddress();
            Instance instance =instanceService.getByHostIp(inetAddress.getHostAddress());
            if (instance == null)
                return toHealth(healthStatus.ERROR);
            if (instance.getIsActive()) {
                return toHealth(healthStatus.OK);
            } else {
                return toHealth(healthStatus.INACTIVE);
            }
        } catch (UnknownHostException ignored) {
            return toHealth(healthStatus.ERROR);
        }
    }

    private InstanceVO.Health toHealth(String status) {
        return InstanceVO.Health.builder()
                .status(status)
                .isHealth(status.equals(healthStatus.OK))
                .build();
    }

    /**
     * 注册Opscloud实例
     */
    private void register() {
        try {
            InetAddress inetAddress = HostUtil.getInetAddress();
            // 已存在
            if (instanceService.getByHostIp(inetAddress.getHostAddress()) != null) return;
            Instance instance = Instance.builder()
                    .hostIp(inetAddress.getHostAddress())
                    .hostname(inetAddress.getHostName())
                    .name(inetAddress.getCanonicalHostName())
                    .status(0)
                    .isActive(true)
                    .build();
            instanceService.add(instance);
        } catch (UnknownHostException ignored) {
        }
    }

    @Override
    public void afterPropertiesSet() {
        this.register();
    }

}
