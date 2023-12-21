package com.baiyi.opscloud.facade.sys.impl;

import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.HostUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Instance;
import com.baiyi.opscloud.domain.param.sys.RegisteredInstanceParam;
import com.baiyi.opscloud.domain.vo.sys.InstanceVO;
import com.baiyi.opscloud.facade.sys.InstanceFacade;
import com.baiyi.opscloud.packer.sys.RegisteredInstancePacker;
import com.baiyi.opscloud.service.sys.InstanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.common.base.Global.ENV_PROD;

/**
 * @Author baiyi
 * @Date 2021/9/3 1:31 下午
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InstanceFacadeImpl implements InstanceFacade, InitializingBean {

    @Value("${spring.profiles.active}")
    private String env;

    private final InstanceService instanceService;

    private final RegisteredInstancePacker registeredInstancePacker;

    private static final InetAddress INET_ADDRESS = getInetAddress();

    public interface HealthStatus {
        String OK = "OK";
        String ERROR = "ERROR";
        String INACTIVE = "INACTIVE";
    }

    @Override
    public DataTable<InstanceVO.RegisteredInstance> queryRegisteredInstancePage(RegisteredInstanceParam.RegisteredInstancePageQuery pageQuery) {
        DataTable<Instance> table = instanceService.queryRegisteredInstancePage(pageQuery);
        List<InstanceVO.RegisteredInstance> data = BeanCopierUtil.copyListProperties(table.getData(), InstanceVO.RegisteredInstance.class).stream()
                .peek(e -> registeredInstancePacker.wrap(e, pageQuery)).collect(Collectors.toList());
        return new DataTable<>(
                data,
                table.getTotalNum());
    }

    @Override
    public void setRegisteredInstanceActive(int id) {
        Instance instance = instanceService.getById(id);
        if (instance == null) {
            return;
        }
        if (instance.getIsActive()) {
            List<Instance> instanceList = instanceService.listActiveInstance();
            if (instanceList.size() <= 1) {
                throw new OCException("至少保留一个可用实例");
            }
        }
        instance.setIsActive(!instance.getIsActive());
        instanceService.update(instance);
        log.info("用户修改注册实例: isActive={}", instance.getIsActive());
    }

    @Override
    public boolean isHealth() {
        InstanceVO.Health health = checkHealth();
        return HealthStatus.OK.equals(health.getStatus());
    }

    private static InetAddress getInetAddress() {
        try {
            return HostUtil.getInetAddress();
        } catch (UnknownHostException ignored) {
            return null;
        }
    }

    @Override
    public InstanceVO.Health checkHealth() {
        if (InstanceFacadeImpl.INET_ADDRESS == null) {
            return buildHealthWithStatus(HealthStatus.ERROR);
        }
        Instance instance = instanceService.getByHostIp(InstanceFacadeImpl.INET_ADDRESS.getHostAddress());
        if (instance == null) {
            return buildHealthWithStatus(HealthStatus.ERROR);
        }
        if (instance.getIsActive()) {
            return buildHealthWithStatus(HealthStatus.OK);
        } else {
            return buildHealthWithStatus(HealthStatus.INACTIVE);
        }
    }

    private InstanceVO.Health buildHealthWithStatus(String status) {
        return InstanceVO.Health.builder()
                .status(status)
                .isHealth(status.equals(HealthStatus.OK))
                .build();
    }

    /**
     * 注册Opscloud实例
     */
    private void register() throws UnknownHostException {
        if (!ENV_PROD.equals(env)) {
            return;
        }
        InetAddress inetAddress = HostUtil.getInetAddress();
        // 已存在
        if (instanceService.getByHostIp(inetAddress.getHostAddress()) != null) {
            return;
        }
        Instance instance = Instance.builder()
                .hostIp(inetAddress.getHostAddress())
                .hostname(inetAddress.getHostName())
                .name(inetAddress.getCanonicalHostName())
                .status(0)
                .isActive(true)
                .build();
        instanceService.add(instance);
    }

    /**
     * 查询自己
     *
     * @return
     * @throws UnknownHostException
     */
    @Override
    public Instance getInstance() throws UnknownHostException {
        InetAddress inetAddress = HostUtil.getInetAddress();
        return instanceService.getByHostIp(inetAddress.getHostAddress());
    }

    @Override
    public void afterPropertiesSet() throws UnknownHostException {
        this.register();
    }

}