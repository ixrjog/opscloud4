package com.sdg.cmdb.service.control.configurationfile;

import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.template.format.configurationitem.TomcatInstallConfigOpt;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liangjian on 2016/12/14.
 */
@Service
public class TomcatInstallConfigService extends ConfigurationFileControlAbs {


    public String acqConfig() {
        String result = getHeadInfo();
        TomcatInstallConfigOpt tico = new TomcatInstallConfigOpt();
        List<ServerGroupDO> serverGroups = serverGroupDao.queryServerGroup();
        if (serverGroups == null || serverGroups.get(0) == null) return result;
        //List<ServerGroupDO> groups = new ArrayList<>();
        for (ServerGroupDO group : serverGroups) {
            //List<ServerDO> hosts = serverDao.acqServersByGroupId(group.getId());
            //if (hosts.isEmpty())
            //    continue;
            String tomcatVersion = configServerGroupService.queryTomcatInstallVersion(group);
            String javaVersion = configServerGroupService.queryJavaInstallVersion(group);
            if (tomcatVersion == null || tomcatVersion.isEmpty()) continue;
            if (javaVersion == null || javaVersion.isEmpty()) continue;
            String groupName = group.getName().replace("group_", "");
            tico.put(TomcatInstallConfigOpt.buider(groupName, javaVersion, tomcatVersion));
        }
        result += tico.toBody();
        return result;
    }





}