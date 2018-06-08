package com.sdg.cmdb.service.control.configurationfile;

import com.sdg.cmdb.dao.cmdb.DnsDao;
import com.sdg.cmdb.domain.dns.DnsmasqDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

import java.util.List;

/**
 * Created by liangjian on 2017/7/11.
 */
@Service
public class DnsmasqService extends ConfigurationFileControlAbs {

    public static final String HOST_DOMAIN = "51xianqu.net";

    @Resource
    private DnsDao dnsDao;

    public String acqConfig(int type) {

        String result = getHeadInfo();
        List<DnsmasqDO> listSystemItems = dnsDao.queryDnsmasqByGroupIdAndItemType(type, DnsmasqDO.ItemTypeEnum.system.getCode());
        List<DnsmasqDO> listServerItems = dnsDao.queryDnsmasqByGroupIdAndItemType(type, DnsmasqDO.ItemTypeEnum.server.getCode());
        List<DnsmasqDO> listAddressItems = dnsDao.queryDnsmasqByGroupIdAndItemType(type, DnsmasqDO.ItemTypeEnum.address.getCode());

        for (DnsmasqDO dnsmasqDO : listSystemItems)
            result += buildItem(dnsmasqDO);
        result += "\n";

        for (DnsmasqDO dnsmasqDO : listServerItems)
            result += buildItem(dnsmasqDO);
        result += "\n";

        for (DnsmasqDO dnsmasqDO : listAddressItems)
            result += buildItem(dnsmasqDO);
        result += "\n";

        result += buildServerGroup();

        return result;
    }


    private String buildItem(DnsmasqDO dnsmasqDO) {
        String itemLine = "";
        if (!StringUtils.isEmpty(dnsmasqDO.getContent()))
            itemLine += "# " + dnsmasqDO.getContent() + "\n";
        itemLine += dnsmasqDO.getItem() + "=" + dnsmasqDO.getItemValue() + "\n";
        return itemLine;
    }

    private String buildServerGroup() {
        String result = "";
        List<ServerGroupDO> listServerGroupDO = acqServerGroupByWebservice();
        for (ServerGroupDO serverGroupDO : listServerGroupDO) {
            String tomcatName = configServerGroupService.queryTomcatAppName(serverGroupDO);
            if (tomcatName == null || tomcatName.isEmpty()) continue;
            //List<ServerDO> listServerDO = new ArrayList<ServerDO>();
            result += buildServer(serverGroupDO, tomcatName);
            System.err.println(result);
        }
        return result;
    }

    private String buildServer(ServerGroupDO serverGroupDO, String tomcatName) {
        String result = "";
        List<ServerDO> servers = acqServerByGroup(serverGroupDO);
        for (ServerDO serverDO : servers) {
            // address=/sdg.org/10.17.1.64
            String line = "address=/";
            line += "s" + serverDO.getSerialNumber() + "." + ServerDO.EnvTypeEnum.getEnvTypeName(serverDO.getEnvType()) + "." + tomcatName + "." + HOST_DOMAIN;
            line += "/" + serverDO.getInsideIp() + "\n";
            result += line;
        }
        return result;
    }


}
