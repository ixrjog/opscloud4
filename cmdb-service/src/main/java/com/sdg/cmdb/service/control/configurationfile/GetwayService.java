package com.sdg.cmdb.service.control.configurationfile;

import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.template.getway.Getway;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liangjian on 2016/12/15.
 */
@Service
public class GetwayService extends ConfigurationFileControlAbs {


    public String acqHostConfig() {
        String result = "";
        List<ServerGroupDO> serverGroups = serverGroupDao.queryServerGroup();
        if (serverGroups == null || serverGroups.get(0) == null) return result;
        Map<String, List<ServerDO>> servers = new HashMap<>();
        for (ServerGroupDO serverGroup : serverGroups) {
            List<ServerDO> hosts = serverDao.acqServersByGroupId(serverGroup.getId());
            invokeGetwayIp(hosts);
            if (hosts.isEmpty())
                continue;
            servers.put(serverGroup.getName(), hosts);
        }
        Getway gw = new Getway(serverGroups, servers);
        result = gw.toString();
        return result;
    }

    private void invokeGetwayIp(List<ServerDO> hosts) {
        if (hosts == null) return;
        for (ServerDO host : hosts)
            configServerGroupService.invokeGetwayIp(host);
    }

}
