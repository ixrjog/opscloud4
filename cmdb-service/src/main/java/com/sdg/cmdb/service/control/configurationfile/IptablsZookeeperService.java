package com.sdg.cmdb.service.control.configurationfile;

import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.service.impl.ConfigServerGroupServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangjian on 2017/3/27.
 */
@Service
public class IptablsZookeeperService extends ConfigurationFileControlAbs {

    public static final String dubbo_port = "2181";

    private String acqIptablesHaed(int envCode) {
        String head;
        head = "# zookeeper " + ServerDO.EnvTypeEnum.getEnvTypeName(envCode) + "\n";
        head += "\n";
        head += "# office\n";
        head += "-A INPUT -s 115.236.161.16/28 -j ACCEPT\n";
        head += "-A INPUT -s 124.160.28.144/29 -j ACCEPT\n";
        head += "\n";
        head += "# dubbo\n";
        head += "-I INPUT -p tcp --dport 2181 -j DROP\n";
        head += "-I INPUT -s 127.0.0.0/8 -p tcp -m multiport --dports 2181 -j ACCEPT\n";
        head += "\n";
        return head;
    }




    /**
     * 按类型build
     * 0: daily
     *
     * @param type
     */
    public String acqConfig(int type) {
        int envCode = type;
        String reslut = acqIptablesHaed(envCode);
        List<String> projects = new ArrayList<String>();
        List<ServerGroupDO> listServerGroupDO = acqServerGroupByWebservice();

        // dubbo + zookeeper
        ServerGroupDO dubboServerGroupDO = serverGroupDao.queryServerGroupByName("group_dubbo");
        List<ServerDO> listDubboServerDO = acqServerByGroup(dubboServerGroupDO, envCode);
        reslut += buildIptables(dubboServerGroupDO, listDubboServerDO, envCode, projects) + "\n";

        for (ServerGroupDO serverGroupDO : listServerGroupDO) {
            if (!isbuildIptablesDubbo(serverGroupDO)) continue;
            List<ServerDO> listServerDO = new ArrayList<>();
            if (envCode == ServerDO.EnvTypeEnum.gray.getCode() && configServerGroupService.isIptablesDubboGrayEqProd(serverGroupDO)) {
                listServerDO = acqServerByGroup(serverGroupDO, ServerDO.EnvTypeEnum.prod.getCode());
            } else {
                listServerDO = acqServerByGroup(serverGroupDO, envCode);
            }
            // 无服务器
            if (listServerDO.size() == 0)
                continue;

            String iptables = buildIptables(serverGroupDO, listServerDO, envCode, projects);
            // back环境也加入prod白名单
            if (envCode == ServerDO.EnvTypeEnum.prod.getCode()) {
                List<ServerDO> listBackServerDO = acqServerByGroup(serverGroupDO, ServerDO.EnvTypeEnum.back.getCode());
                // 无服务器
                if (listBackServerDO.size() != 0)
                    iptables += buildIptables(serverGroupDO, listBackServerDO, ServerDO.EnvTypeEnum.back.getCode(), projects);
            }
            if (iptables != null)
                reslut += iptables + "\n";
        }
        // System.err.println(this.projects);
        return reslut;
    }

    private String buildIptables(ServerGroupDO serverGroupDO, List<ServerDO> listServerDO, int envCode, List<String> projects) {
        String result;
        result = "# " + serverGroupDO.getName() + " " + serverGroupDO.getContent() + "\n";
        for (ServerDO serverDO : listServerDO) {
            result += "# " + ServerDO.EnvTypeEnum.getEnvTypeName(serverDO.getEnvType()) + "-" + serverDO.getSerialNumber() + "\n";
            result += "-I INPUT -s " + serverDO.getInsideIp() + " -p tcp -m multiport --dports " + dubbo_port + " -j ACCEPT\n";
        }
        projects.add(serverGroupDO.getName());
        return result;
    }

    public boolean isbuildIptablesDubbo(ServerGroupDO serverGroupDO) {
        // default  true
        String result = configService.acqConfigByServerGroupAndKey(serverGroupDO, ConfigServerGroupServiceImpl.iptables_dubbo_build);
        if (result != null && result.equalsIgnoreCase("false")) return false;
        return true;
    }


}
