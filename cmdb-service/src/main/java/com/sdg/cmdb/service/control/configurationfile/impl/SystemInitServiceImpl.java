package com.sdg.cmdb.service.control.configurationfile.impl;

import com.sdg.cmdb.dao.cmdb.IPGroupDao;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.PublicItemEnum;
import com.sdg.cmdb.domain.ip.IPNetworkDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.service.ConfigCenterService;
import com.sdg.cmdb.service.control.configurationfile.SystemInitService;
import com.sdg.cmdb.util.IPUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by liangjian on 2017/7/27.
 */
@Service
public class SystemInitServiceImpl implements SystemInitService {

    @Resource
    private ConfigCenterService configCenterService;

    @Resource
    private IPGroupDao ipGroupDao;

    private HashMap<String, String> configMap;

    private HashMap<String, String> acqConifMap() {
        if (configMap != null) return configMap;
        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.PUBLIC.getItemKey());
    }

    /**
     * # 新系统修改ip和hostname
     * HOST_NAME=kibana
     *
     * @param serverDO
     * @return
     */
    @Override
    public String acqSystemInitHostConfig(ServerDO serverDO) {
        String result = getHeadInfo();
        result += acqHostname(serverDO);
        result += "\n";
        result += acqNetworkConfig(serverDO);
        return result;
    }

    /**
     * 获取DMZ ipNetwork配置
     *
     * @return
     */
    private IPNetworkDO acqDMZIpNetwork() {
        HashMap<String, String> configMap = acqConifMap();
        String dmzIpNetworkId = configMap.get(PublicItemEnum.OFFICE_DMZ_IP_NETWORK_ID.getItemKey());
        return ipGroupDao.queryIPGroupInfo(Long.valueOf(dmzIpNetworkId));
    }

    private String getHeadInfo() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        return "# Created by cmdb on " + fastDateFormat.format(new Date()) + "\n\n";
    }

    /**
     * 获取主机名配置
     * # 新系统修改ip和hostname
     * HOST_NAME=kibana
     *
     * @param serverDO
     * @return
     */
    private String acqHostname(ServerDO serverDO) {
        String result = "# 新系统修改hostname" + "\n";
        result += "HOST_NAME=" + serverDO.getServerName() + "\n";
        return result;
    }

    /**
     * # auto_set_eth
     * SET_IPADDR=10.17.1.246
     * SET_GATEWAY=10.17.1.254
     * SET_NETMASK=255.255.254.0
     * SET_DNS1=10.16.100.129
     * SET_PREFIX=23
     *
     * @return
     */
    private String acqNetworkConfig(ServerDO serverDO) {
        IPNetworkDO ipNetworkDO = acqDMZIpNetwork();
        String[] networkConfig = ipNetworkDO.getIpNetwork().split("/");
        //netwokrPrefix  网络前缀
        String netwokrPrefix = networkConfig[1];
        //networkMask 子网掩码
        String networkMask = IPUtils.getMaskByMaskBit(netwokrPrefix);

        String result = "# auto_set_eth" + "\n";
        result += "SET_IPADDR=" + serverDO.getInsideIp() + "\n";
        result += "SET_GATEWAY=" + ipNetworkDO.getGateWay() + "\n";
        result += "SET_NETMASK=" + networkMask + "\n";
        result += "SET_DNS1=" + ipNetworkDO.getDnsOne() + "\n";
        result += "SET_PREFIX=" + netwokrPrefix + "\n";
        return result;
    }


}
