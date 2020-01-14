package com.baiyi.opscloud.cloudserver.impl;

import com.baiyi.opscloud.cloudserver.ICloudserver;
import com.baiyi.opscloud.cloudserver.base.CloudserverStatus;
import com.baiyi.opscloud.cloudserver.factory.CloudserverFactory;
import com.baiyi.opscloud.common.util.JSONUtils;
import com.baiyi.opscloud.common.util.ServerUtils;
import com.baiyi.opscloud.domain.generator.OcCloudserver;
import com.baiyi.opscloud.domain.generator.OcServer;
import com.baiyi.opscloud.facade.OcServerFacde;
import com.baiyi.opscloud.service.OcCloudserverService;
import com.baiyi.opscloud.service.OcServerService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
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
public abstract class BaseCloudserver<T> implements InitializingBean, ICloudserver {

    @Resource
    private OcCloudserverService ocCloudserverService;

    @Resource
    private OcServerService ocServerService;

    @Resource
    private OcServerFacde ocServerFacde;

    /**
     * 同步接口
     *
     * @return
     */
    @Override
    public Boolean sync() {
        return sync(Boolean.FALSE);
    }

    @Override
    public Boolean sync(boolean pushName) {
        Map<String, OcCloudserver> cloudserverMap = getCloudserverMap(Lists.newArrayList());
        List<T> instanceList = getInstanceList();
        for (T instance : instanceList)
            saveOcCloudserver(instance, cloudserverMap, pushName);

        return Boolean.TRUE;
    }

    abstract protected String getInstanceId(T instance) throws Exception;

    /**
     * 保存实例信息
     *
     * @param instance
     * @param map
     * @param pushName 是否更新云端主机名
     */
    protected void saveOcCloudserver(T instance, Map<String, OcCloudserver> map, boolean pushName) {
        try {
            String instanceId = getInstanceId(instance);
            if (map.containsKey(instanceId)) {
                OcCloudserver ocCloudserver = map.get(instanceId);
                // 云服务器名称不为空 && 推送主机名
                if (pushName && !StringUtils.isEmpty(ocCloudserver.getServerName())) {
                    String instanceName = getInstanceName(instance);
                    // 比对名称 确认推送
                    if (!ocCloudserver.getServerName().equals(instanceName)) {
                        ocCloudserver.setInstanceName(ocCloudserver.getServerName());
                        log.info("更新云服务器名称, 服务器名 = {} , 原实例名 = {}", ocCloudserver.getServerName(), instanceName);
                        // pushInstanceName(instance, cloudServerDO.getServerName());
                    }
                }
                updateCloudserver(instance, ocCloudserver);
                // TODO 更新 server 表 public_ip

                // saveServerPublicIP(updateCloudServerByInstance(instance, cloudServerDO)); // 已录入(更新数据)
                map.remove(instanceId);
            } else {
                addOcCloudserver(instance);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public Boolean update(String regionId, String instanceId) {
        T instance = getInstance(regionId, instanceId);
        OcCloudserver ocCloudserver = ocCloudserverService.queryOcCloudserverByInstanceId(instanceId);
        return updateCloudserver(instance, ocCloudserver) != null;
    }

    abstract protected T getInstance(String regionId, String instanceId);

    protected void addOcCloudserver(T instance) {
        addOcCloudserver(getCloudserver(instance));
    }

    protected Map<String, OcCloudserver> getCloudserverMap(List<OcCloudserver> cloudserverList) {
        if (CollectionUtils.isEmpty(cloudserverList))
            cloudserverList = ocCloudserverService.queryOcCloudserverByType(getCloudserverType());
        return cloudserverList.stream().collect(Collectors.toMap(OcCloudserver::getInstanceId, a -> a, (k1, k2) -> k1));
    }

    abstract protected int getCloudserverType();

    abstract protected String getInstanceName(T instance);

    abstract protected List<T> getInstanceList();

    @Override
    public String getKey() {
        return this.getClass().getSimpleName();
    }

    protected String getInstanceDetail(Object instance) {
        return JSONUtils.writeValueAsString(instance);
    }

    protected void addOcCloudserver(OcCloudserver ocCloudserver) {
        if (ocCloudserver == null) return;
        invokeCloudserverStatus(ocCloudserver, CloudserverStatus.CREATE.getStatus());
        ocCloudserverService.addOcCloudserver(ocCloudserver);
    }


    /**
     * 更新
     *
     * @param instance
     * @param ocCloudserver
     * @return
     */
    protected OcCloudserver updateCloudserver(T instance, OcCloudserver ocCloudserver) {
        OcCloudserver preCloudserver = getCloudserver(instance);
        preCloudserver.setId(ocCloudserver.getId());
        invokeCloudserverStatus(preCloudserver, ocCloudserver.getServerStatus());
        if (preCloudserver != null)
            ocCloudserverService.updateOcCloudserver(preCloudserver);
        return preCloudserver;
    }

    /**
     * 获取预更新 Cloudserver
     *
     * @param instance
     * @return
     */
    abstract protected OcCloudserver getCloudserver(T instance);

    /**
     * 设置云服务器状态
     *
     * @param ocCloudserver
     * @param cloudserverStatus
     */
    private void invokeCloudserverStatus(OcCloudserver ocCloudserver, int cloudserverStatus) {
        if (cloudserverStatus == CloudserverStatus.CREATE.getStatus() || cloudserverStatus == CloudserverStatus.REGISTER.getStatus()) {
            OcServer ocServer = ocServerService.queryOcServerByPrivateIp(ocCloudserver.getPrivateIp());
            if (ocServer == null) {
                ocCloudserver.setServerStatus(CloudserverStatus.CREATE.getStatus());
            } else {
                ocCloudserver.setServerStatus(CloudserverStatus.REGISTER.getStatus());
                ocCloudserver.setServerId(ocServer.getId());
                ocCloudserver.setServerName(ServerUtils.toServerName(ocServerFacde.getOcServerBO(ocServer)));
            }
        }
    }

    /**
     * 注册
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        CloudserverFactory.register(this);
    }

}
