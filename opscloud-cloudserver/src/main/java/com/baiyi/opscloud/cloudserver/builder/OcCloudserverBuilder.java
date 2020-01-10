package com.baiyi.opscloud.cloudserver.builder;

import com.baiyi.opscloud.cloudserver.base.CloudserverType;
import com.baiyi.opscloud.cloudserver.instance.ZabbixHostInstance;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.OcCloudserver;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostInterface;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;


/**
 * @Author baiyi
 * @Date 2019/11/27 4:30 PM
 * @Version 1.0
 */
public class OcCloudserverBuilder {

    /**
     * Zabbix Host
     *
     * @param hostInstance
     * @return
     */
    public static OcCloudserver buildOcCloudserver(ZabbixHostInstance hostInstance, String instanceDetail, String zone) {
        String privateIp = "";
        if (!CollectionUtils.isEmpty(hostInstance.getInterfaceList()))
            for (ZabbixHostInterface hostInterface : hostInstance.getInterfaceList()) {
                if (hostInterface.getType().equals("1") && !StringUtils.isEmpty(hostInterface.getIp())) {
                    privateIp = hostInterface.getIp();
                    break;
                }
            }
        OcCloudserverBO ocCloudserverBO = OcCloudserverBO.builder()
                .instanceType("ZabbixHost")
                .instanceName(hostInstance.getHost().getName())
                .instanceId(hostInstance.getHost().getHostid())
                .serverName(hostInstance.getHost().getName())
                .instanceDetail(hostInstance.getHost().getHostid())
                .cloudserverType(CloudserverType.ZH.getType())
                .privateIp(privateIp)
                .instanceDetail(instanceDetail)
                .createdTime(new Date())
                .zone(zone)
                .build();
        return BeanCopierUtils.copyProperties(ocCloudserverBO, OcCloudserver.class);
    }

//    public static CloudServerDO buildCloudServerDO(ServerDO serverDO, int serverStatus, int cloudServerType) {
//        CloudServerDO cloudServerDO = new CloudServerDO();
//        cloudServerDO.setServerId(serverDO.getId());
//        cloudServerDO.setServerName(serverDO.acqServerName());
//        cloudServerDO.setServerStatus(serverStatus);
//        cloudServerDO.setPrivateIp(serverDO.getInsideIp());
//        if (!StringUtils.isEmpty(serverDO.getPublicIp()))
//            cloudServerDO.setPublicIp(serverDO.getPublicIp());
//        if (!StringUtils.isEmpty(serverDO.getArea()))
//            cloudServerDO.setZone(serverDO.getArea());
//        cloudServerDO.setCloudServerType(cloudServerType);
//        return cloudServerDO;
//    }
}
