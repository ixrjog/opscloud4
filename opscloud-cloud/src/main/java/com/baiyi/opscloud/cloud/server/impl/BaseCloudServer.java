package com.baiyi.opscloud.cloud.server.impl;

import com.baiyi.opscloud.cloud.server.ICloudServer;
import com.baiyi.opscloud.cloud.server.factory.CloudServerFactory;
import com.baiyi.opscloud.common.base.CloudServerStatus;
import com.baiyi.opscloud.common.cloud.BaseCloudServerInstance;
import com.baiyi.opscloud.common.util.JSONUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.service.cloud.OcCloudServerService;
import com.baiyi.opscloud.service.env.OcEnvService;
import com.baiyi.opscloud.service.server.OcServerService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/1/9 3:39 下午
 * @Version 1.0
 */
@Slf4j
@Component
public abstract class BaseCloudServer<T extends BaseCloudServerInstance> implements InitializingBean, ICloudServer {

    @Resource
    protected OcCloudServerService ocCloudServerService;

    @Resource
    private OcServerService ocServerService;

    @Resource
    private OcEnvService ocEnvService;

    public final static boolean NOT_PUSH_NAME = false;

    public static final boolean POWER_ON = true;
    public static final boolean POWER_OFF = false;


    /**
     * 同步接口
     *
     * @return
     */
    @Override
    public void sync() {
        sync(NOT_PUSH_NAME);
    }

    /**
     * 录入实例
     *
     * @param regionId
     * @param instanceId
     * @return
     */
    @Override
    public void record(String regionId, String instanceId) {
        T instance = getInstance(regionId, instanceId);
        saveCloudServer(instance, Maps.newHashMap(), NOT_PUSH_NAME);
    }

    @Override
    public void sync(boolean pushName) {
        Map<String, OcCloudServer> cloudServerMap = getCloudServerMap(Lists.newArrayList());
        getInstanceList().forEach(i -> saveCloudServer(i, cloudServerMap, pushName));
        setCloudServerDeleted(cloudServerMap);
    }

    abstract protected String getInstanceId(T instance) throws Exception;

    /**
     * 保存实例信息
     *
     * @param instance
     * @param map
     * @param isPushName 是否推送云端主机名
     */
    protected void saveCloudServer(T instance, Map<String, OcCloudServer> map, boolean isPushName) {
        try {
            String instanceId = getInstanceId(instance);
            if (map.containsKey(instanceId)) {
                updateCloudServer(instance, map.get(instanceId), isPushName);
                // TODO 更新 server 表 public_ip
                // saveServerPublicIP(updateCloudServerByInstance(instance, cloudServerDO)); // 已录入(更新数据)
            } else {
                addOcCloudServer(instance);
            }
            map.remove(instanceId);
        } catch (Exception ignored) {
        }
    }

    private void pushInstanceName(T instance, OcCloudServer ocCloudServer) {
        if (!StringUtils.isEmpty(ocCloudServer.getServerName())) {
            String instanceName = getInstanceName(instance);
            // 比对名称 确认推送
            if (!ocCloudServer.getServerName().equals(instanceName)) {
                // ocCloudServer.setInstanceName(ocCloudServer.getServerName());
                log.info("更新云服务器名称, 服务器名 = {} , 原实例名 = {}", ocCloudServer.getServerName(), instanceName);
                pushInstanceName(instance, ocCloudServer.getServerName());
            }
        }
    }

    protected boolean pushInstanceName(T instance, String instanceName) {
        return false;
    }

    private void setCloudServerDeleted(Map<String, OcCloudServer> map) {
        if (map == null || map.isEmpty()) return;
        map.keySet().forEach(k -> {
            OcCloudServer ocCloudServer = map.get(k);
            ocCloudServer.setServerStatus(CloudServerStatus.DELETE.getStatus());
            ocCloudServerService.updateOcCloudServer(ocCloudServer);
        });
    }

    @Override
    public Boolean update(String regionId, String instanceId) {
        T instance = getInstance(regionId, instanceId);
        OcCloudServer ocCloudServer = ocCloudServerService.queryOcCloudServerByInstanceId(instanceId);
        return updateCloudServer(instance, ocCloudServer, NOT_PUSH_NAME) != null;
    }

    abstract protected T getInstance(String regionId, String instanceId);

    protected void addOcCloudServer(T instance) {
        addOcCloudServer(getCloudServer(instance));
    }

    protected Map<String, OcCloudServer> getCloudServerMap(List<OcCloudServer> cloudServerList) {
        if (CollectionUtils.isEmpty(cloudServerList))
            cloudServerList = ocCloudServerService.queryOcCloudServerByType(getCloudServerType());
        return cloudServerList.stream().collect(Collectors.toMap(OcCloudServer::getInstanceId, a -> a, (k1, k2) -> k1));
    }

    /**
     * 取云服务器类型
     *
     * @return
     */
    abstract protected int getCloudServerType();

    /**
     * 取云服务器名称
     *
     * @param instance
     * @return
     */
    abstract protected String getInstanceName(T instance);

    /**
     * 全量查询实例信息
     *
     * @return
     */
    abstract protected List<T> getInstanceList();

    @Override
    public String getKey() {
        return this.getClass().getSimpleName();
    }

    protected String getInstanceDetail(Object instance) {
        return JSONUtils.writeValueAsString(instance);
    }

    protected void addOcCloudServer(OcCloudServer ocCloudServer) {
        if (ocCloudServer == null) return;
        assembleCloudServerStatus(ocCloudServer, CloudServerStatus.CREATE.getStatus());
        ocCloudServerService.addOcCloudServer(ocCloudServer);
    }


    /**
     * 更新
     *
     * @param instance      实例
     * @param ocCloudServer 原数据
     * @param isPushName    是否推送名称
     * @return
     */
    protected OcCloudServer updateCloudServer(T instance, OcCloudServer ocCloudServer, boolean isPushName) {
        OcCloudServer pre = getCloudServer(instance);
        pre.setId(ocCloudServer.getId());
        // 推送主机名
        if (isPushName) {
            log.info("更新云服务器名称: ip = {}, serverName = {} -> {}", ocCloudServer.getPrivateIp(), pre.getServerName(), ocCloudServer.getServerName());
            pushInstanceName(instance, ocCloudServer);
            pre.setInstanceName(ocCloudServer.getServerName());
            pre.setServerName(ocCloudServer.getServerName());
        }
        pre.setPowerMgmt(ocCloudServer.getPowerMgmt());
        int cloudServerStatus = ocCloudServer.getServerStatus() == null ? 0 : ocCloudServer.getServerStatus();
        assembleCloudServerStatus(pre, cloudServerStatus);
        if (StringUtils.isEmpty(pre.getPrivateIp())) // VM-Tools可能导致获取不到ip
            pre.setPrivateIp(ocCloudServer.getPrivateIp());
        ocCloudServerService.updateOcCloudServer(pre);
        return pre;
    }

    /**
     * 获取预更新 CloudServer
     *
     * @param instance
     * @return
     */
    abstract protected OcCloudServer getCloudServer(T instance);

    /**
     * 设置云服务器状态
     *
     * @param ocCloudServer
     * @param cloudServerStatus
     */
    private void assembleCloudServerStatus(OcCloudServer ocCloudServer, int cloudServerStatus) {
        if (cloudServerStatus == CloudServerStatus.CREATE.getStatus() || cloudServerStatus == CloudServerStatus.REGISTER.getStatus()) {
            OcServer ocServer = ocServerService.queryOcServerByPrivateIp(ocCloudServer.getPrivateIp());
            if (ocServer == null) {
                ocCloudServer.setServerStatus(CloudServerStatus.CREATE.getStatus());
            } else {
                ocCloudServer.setServerStatus(CloudServerStatus.REGISTER.getStatus());
                ocCloudServer.setServerId(ocServer.getId());
            }
        }
    }

    /**
     * 开机
     *
     * @param id
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> start(Integer id) {
        OcCloudServer ocCloudServer = ocCloudServerService.queryOcCloudServerById(id);
        if (!checkAuth(ocCloudServer))
            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_FAILUER);
        return power(ocCloudServer, POWER_ON);
    }

    /**
     * 关机
     *
     * @param id
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> stop(Integer id) {
        OcCloudServer ocCloudServer = ocCloudServerService.queryOcCloudServerById(id);
        if (!checkAuth(ocCloudServer))
            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_FAILUER);
        return power(ocCloudServer, POWER_OFF);
    }

    /**
     * 如果支持电源管理请重写
     *
     * @param action
     * @return
     */
    protected BusinessWrapper<Boolean> power(OcCloudServer ocCloudServer, Boolean action) {
        return BusinessWrapper.SUCCESS;
    }

    /**
     * 如果支持释放请重写
     *
     * @param ocCloudServer
     * @return
     */
    protected Boolean delete(OcCloudServer ocCloudServer) {
        return true;
    }

    @Override
    public void modifyInstanceChargeType(String instanceId, String chargeType) {
        OcCloudServer ocCloudServer = ocCloudServerService.queryOcCloudServerByInstanceId(instanceId);
        modifyInstanceChargeType(ocCloudServer, chargeType);
    }

    /**
     * 如果支持修改付费策略请重写
     *
     * @param ocCloudServer
     * @return
     */
    protected void modifyInstanceChargeType(OcCloudServer ocCloudServer, String chargeType) {
    }


    private Boolean checkAuth(OcCloudServer ocCloudServer) {
        if (!ocCloudServer.getPowerMgmt())
            return Boolean.FALSE;
        return Boolean.TRUE;
    }

    @Override
    public int queryPowerStatus(Integer id) {
        OcCloudServer ocCloudServer = ocCloudServerService.queryOcCloudServerById(id);
        int powerStatus = getPowerStatus(ocCloudServer.getRegionId(), ocCloudServer.getInstanceId());
        ocCloudServer.setPowerStatus(powerStatus);
        ocCloudServerService.updateOcCloudServer(ocCloudServer);
        return powerStatus;
    }

    abstract int getPowerStatus(String regionId, String instanceId);

    @Override
    public void offline(int serverId) {
        OcCloudServer ocCloudServer = ocCloudServerService.queryOcCloudServerByUnqueKey(getCloudServerType(), serverId);
        if (ocCloudServer == null) return;
        ocCloudServer.setServerStatus(CloudServerStatus.OFFLINE.getStatus());
        ocCloudServerService.updateOcCloudServer(ocCloudServer);
    }

    /**
     * 注册
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() {
        CloudServerFactory.register(this);
    }

    @Override
    public Boolean delete(String instanceId) {
        OcCloudServer ocCloudServer = ocCloudServerService.queryOcCloudServerByInstanceId(instanceId);
        return delete(ocCloudServer);
    }


}
