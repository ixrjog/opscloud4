package com.baiyi.opscloud.facade.sys.impl;

import com.baiyi.opscloud.common.util.HostUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Instance;
import com.baiyi.opscloud.domain.param.sys.RegisteredInstanceParam;
import com.baiyi.opscloud.domain.vo.sys.InstanceVO;
import com.baiyi.opscloud.facade.sys.InstanceFacade;
import com.baiyi.opscloud.packer.sys.RegisteredInstancePacker;
import com.baiyi.opscloud.service.sys.InstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/9/3 1:31 下午
 * @Version 1.0
 */
@Slf4j
@Service
public class InstanceFacadeImpl implements InstanceFacade, InitializingBean {

    @Resource
    private InstanceService instanceService;

    @Resource
    private RegisteredInstancePacker registeredInstancePacker;

    public interface healthStatus {
        String OK = "OK";
        String ERROR = "ERROR";
        String INACTIVE = "INACTIVE";
    }

    @Override
    public DataTable<InstanceVO.RegisteredInstance> queryRegisteredInstancePage(RegisteredInstanceParam.RegisteredInstancePageQuery pageQuery) {
        DataTable<Instance> table = instanceService.queryRegisteredInstancePage(pageQuery);
        return new DataTable<>(
                table.getData().stream().map(e -> registeredInstancePacker.wrapToVO(e, pageQuery)).collect(Collectors.toList()),
                table.getTotalNum());
    }

    @Override
    public void setRegisteredInstanceActive(int id) {
        Instance instance = instanceService.getById(id);
        if (instance == null) return;
        instance.setIsActive(!instance.getIsActive());
        instanceService.update(instance);
        log.info("用户修改注册实例: isActive = {}", instance.getIsActive());
    }

    @Override
    public boolean isHealth() {
        InstanceVO.Health health = checkHealth();
        return healthStatus.OK.equals(health.getStatus());
    }

    @Override
    public InstanceVO.Health checkHealth() {
        try {
            InetAddress inetAddress = HostUtil.getInetAddress();
            Instance instance = instanceService.getByHostIp(inetAddress.getHostAddress());
            if (instance == null)
                return buildHealth(healthStatus.ERROR);
            if (instance.getIsActive()) {
                return buildHealth(healthStatus.OK);
            } else {
                return buildHealth(healthStatus.INACTIVE);
            }
        } catch (UnknownHostException ignored) {
            return buildHealth(healthStatus.ERROR);
        }
    }

    private InstanceVO.Health buildHealth(String status) {
        return InstanceVO.Health.builder()
                .status(status)
                .isHealth(status.equals(healthStatus.OK))
                .build();
    }

    /**
     * 注册Opscloud实例
     */
    private void register() throws UnknownHostException {
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
