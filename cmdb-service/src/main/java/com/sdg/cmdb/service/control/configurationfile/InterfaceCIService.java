package com.sdg.cmdb.service.control.configurationfile;

import com.sdg.cmdb.domain.config.ConfigPropertyDO;
import com.sdg.cmdb.domain.config.ServerGroupPropertiesDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangjian on 2016/12/14.
 */

@Service
public class InterfaceCIService extends ConfigurationFileControlAbs {

    // private String interface_ci = "";


    private long projectNameId = 0;
    private long hostGroupId = 0;
    private long environmentelId = 0;

    private long getProjectNameId() {
        if (projectNameId != 0) {
            return projectNameId;
        }
        ConfigPropertyDO c = configDao.getConfigPropertyByName("PROJECT_NAME");
        this.projectNameId = c.getId();
        return projectNameId;
    }

    private long getHostGroupId() {
        if (hostGroupId != 0) {
            return hostGroupId;
        }
        ConfigPropertyDO c = configDao.getConfigPropertyByName("HOST_GROUP");
        this.hostGroupId = c.getId();
        return hostGroupId;
    }

    private long getEnvironmentelId() {
        if (environmentelId != 0) {
            return environmentelId;
        }
        ConfigPropertyDO c = configDao.getConfigPropertyByName("ENVIRONMENTEL");
        this.environmentelId = c.getId();
        return environmentelId;
    }


    private String acqHostGroup(ServerGroupDO group) {
        return acqValueById(group, getHostGroupId());
    }

    private String acqEnvironmentel(ServerGroupDO group) {
        return acqValueById(group, getEnvironmentelId());
    }

    private String acqValueById(ServerGroupDO group, long id) {
        ServerGroupPropertiesDO propertiesDO = new ServerGroupPropertiesDO();
        propertiesDO.setPropertyId(id);
        propertiesDO.setGroupId(group.getId());
        List<ServerGroupPropertiesDO> serverGroupPropertiesDO = configDao.getServerGroupProperty(propertiesDO);
        if (serverGroupPropertiesDO.size() == 0) return null;
        return serverGroupPropertiesDO.get(0).getPropertyValue();
    }




    public String acqConfig() {
        String reslut = getHeadInfo();
        reslut += "JAVA_PROJECTS_OPT=(\n";
        reslut += "# 项目名  主机组前缀  环境  配置文件\n";
        List<ServerGroupDO> serverGroups = serverGroupDao.queryServerGroup();
        if (serverGroups == null || serverGroups.get(0) == null) return reslut;
        List<ServerGroupDO> groups = new ArrayList<>();
        for (ServerGroupDO group : serverGroups) {
            //List<ServerDO> hosts = serverDao.acqServersByGroupId(group.getId());
            //if (hosts.isEmpty()) continue;
            String projectName = configServerGroupService.queryProjectName(group);
            if (projectName == null || projectName.isEmpty()) {
                projectName = group.getName().replace("group_", "");
            }
            String hostGroup = acqHostGroup(group);
            if (hostGroup == null || hostGroup.isEmpty()) {
                hostGroup = projectName;
            }
            String environmentel = acqEnvironmentel(group);
            if (environmentel == null || environmentel.isEmpty()) continue;
            reslut += "# " + group.getContent() + "\n";
            reslut += "'" + projectName + "' '" + hostGroup + "' '" + environmentel + "' '-'\n";
        }
        reslut += ")\n";
        return reslut;
    }




}
