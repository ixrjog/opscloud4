package com.sdg.cmdb.service.control.configurationfile;

import com.sdg.cmdb.domain.config.ConfigPropertyDO;
import com.sdg.cmdb.domain.config.ServerGroupPropertiesDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.service.ConfigService;
import com.sdg.cmdb.service.impl.ConfigServerGroupServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangjian on 2017/3/7.
 */
@Service
public class NginxUpstreamService extends ConfigurationFileControlAbs {



    /**
     * 按类型build
     * 0: daily
     *
     * @param type
     */

    public String acqConfig(int type) {
        int envCode = type;
        List<String> projects = new ArrayList<String>();
        String upstream = "";
        //ServerDO.EnvTypeEnum.daily.getCode();
        List<ServerGroupDO> listServerGroupDO = acqServerGroupByWebservice();

        for (ServerGroupDO serverGroupDO : listServerGroupDO) {
            String tomcatName = configServerGroupService.queryTomcatAppName(serverGroupDO);

            if (tomcatName == null || tomcatName.isEmpty()) continue;
            List<ServerDO> listServerDO = new ArrayList<ServerDO>();
            // 部分灰度环境是用的线上服务器
            if (envCode == ServerDO.EnvTypeEnum.gray.getCode() && configServerGroupService.isGrayEqProd(serverGroupDO)) {
                listServerDO = acqServerByGroup(serverGroupDO, ServerDO.EnvTypeEnum.prod.getCode());
            } else {
                listServerDO = acqServerByGroup(serverGroupDO, envCode);
            }
            if (listServerDO.size() == 0) continue;
            String u = buildUpstream(serverGroupDO, listServerDO, envCode);
            if (u != null) {
                upstream += u + "\n";
                projects.add(serverGroupDO.getName());
            }
        }
        return upstream;
    }

    /**
     * 取check配置
     * check_http_send "GET /market-tool/webStatus HTTP/1.0\r\n\r\n";
     *
     * @param serverGroupDO
     * @return
     */

    private String acqWebStatusConfig(ServerGroupDO serverGroupDO) {
        String indent = ConfigServerGroupServiceImpl.nginx_locaton_indent;
        String indentX2 = indent + indent;
        String result = indentX2 + "check interval=1000 rise=1 fall=3 timeout=1000 type=http;\n";
        return result + indentX2 + "check_http_send \"GET /" + configServerGroupService.queryHttpStatus(serverGroupDO) + " HTTP/1.0\\r\\n\\r\\n\";\n";

    }

    private String buildUpstream(ServerGroupDO serverGroupDO, List<ServerDO> listServerDO, int envCode) {
        String projectName = configServerGroupService.queryProjectName(serverGroupDO);
        String indent = ConfigServerGroupServiceImpl.nginx_locaton_indent;
        String indentX2 = indent + indent;

        if (projectName == null) return null;
        String upstream;
        upstream = indent + "# " + serverGroupDO.getName() + "\n";
        if (serverGroupDO.getContent() != null && !serverGroupDO.getContent().isEmpty())
            upstream += indent + "# " + serverGroupDO.getContent() + "\n";
        if (!configServerGroupService.isBuildNginxUpstream(serverGroupDO)) {
            upstream += indent + "# NGINX_UPSTREAM_BUILD = false\n";
            return upstream;
        }
        String nginxUpstreamName = configServerGroupService.queryNginxUpstreamName(serverGroupDO);
        if (envCode == ServerDO.EnvTypeEnum.prod.getCode()) {
            upstream += indent + "upstream upstream." + nginxUpstreamName + ".java { \n";
        } else {
            upstream += indent + "upstream upstream." + ServerDO.EnvTypeEnum.getEnvTypeName(envCode) + '.' + nginxUpstreamName + ".java { \n";
        }
        for (ServerDO serverDO : listServerDO) {
            upstream += indentX2 + "# " + ServerDO.EnvTypeEnum.getEnvTypeName(serverDO.getEnvType()) + "-" + serverDO.getSerialNumber() + "\n";
            upstream += acqUpstreamServer(serverGroupDO, serverDO);
        }
        if (listServerDO.size() > 1)
            upstream += acqWebStatusConfig(serverGroupDO);
        return upstream + "    }\n";
    }

    private String acqUpstreamServer(ServerGroupDO serverGroupDO, ServerDO serverDO) {
        String indent = ConfigServerGroupServiceImpl.nginx_locaton_indent;
        String indentX2 = indent + indent;
        String tomcatHttpPort = configServerGroupService.queryTomcatHttpPort(serverGroupDO);

        String weight = configServerGroupService.queryNginxUpstreamWeight(serverDO);
        if (!StringUtils.isEmpty(weight) && !weight.equals("1"))
            weight = " weight=" + weight;

        String serverType = configServerGroupService.queryNginxUpstreamServerType(serverDO);
        String maxFails = "";
        String failTimeout = "";
        if (!StringUtils.isEmpty(serverType)) {
            if (serverType.equalsIgnoreCase("down"))
                serverType = " down";
            if (serverType.equalsIgnoreCase("backup"))
                serverType = " backup";
        } else {
            //参数不能同时拥有
            maxFails = configServerGroupService.queryNginxUpstreamMaxFails(serverGroupDO);
            if (!StringUtils.isEmpty(maxFails))
                maxFails = " max_fails=" + maxFails;

            failTimeout = configServerGroupService.queryNginxUpstreamFailTimeout(serverGroupDO);
            if (!StringUtils.isEmpty(maxFails))
                failTimeout = " fail_timeout=" + failTimeout;
        }
        String result = indentX2 + "server " + serverDO.getInsideIp() + ":" + tomcatHttpPort + weight + serverType + maxFails + failTimeout + ";\n";
        return result;
    }


}
