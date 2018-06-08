package com.sdg.cmdb.service.control.configurationfile;

import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.service.impl.ConfigServerGroupServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangjian on 2017/3/14.
 * manage.51xianqu.com
 */
@Service
public class NginxLocationManageService extends ConfigurationFileControlAbs {




    public  String acqDomain(){
        return "manage.51xianqu.com";
    }


    /**
     * 按类型build
     * 0: daily
     *
     * @param type
     */

    public String acqConfig(int type) {
        int envCode = type;
        return buildLocation(envCode);
    }

    @Override
    protected boolean isBuildLocation(ServerGroupDO serverGroupDO){
        return configServerGroupService.isBuildManageLocation(serverGroupDO);
    }

    @Override
    protected String buildLocation(ServerGroupDO serverGroupDO, int envCode, List<String> projects) {
        String projectName = configServerGroupService.queryProjectName(serverGroupDO);
        if (projectName == null) return null;
        String result;
        if (serverGroupDO.getContent() != null && !serverGroupDO.getContent().isEmpty()) {
            result = "# " + serverGroupDO.getContent() + "\n";
        } else {
            result = "# " + serverGroupDO.getName() + "\n";
        }
        /**
         *
         location ~ ^/sdop/ {
         proxy_pass http://upstream.sdop.java;
         include /usr/local/nginx/conf/vhost/proxy_default.conf;
         }
         */
        result += "location " + configServerGroupService.queryNginxUrl(serverGroupDO) + "/ {\n";
        // 增加location扩展参数
        result += acqLocationParam(serverGroupDO);

        String nginxProxyPassTail = configService.acqConfigByServerGroupAndKey(serverGroupDO, ConfigServerGroupServiceImpl.nginx_proxy_pass_tail);
        String nginxUpstreamName = configServerGroupService.queryNginxUpstreamName(serverGroupDO);
        if (configServerGroupService.isManageBack(serverGroupDO, envCode)) {

            if (org.springframework.util.StringUtils.isEmpty(nginxProxyPassTail)) {
                result += "    proxy_pass http://upstream.back." + nginxUpstreamName + ".java;\n";
            } else {
                result += "    proxy_pass http://upstream.back." + nginxUpstreamName + ".java" + nginxProxyPassTail + " ;\n";
            }
        } else {
            String envName = configServerGroupService.queryEnvName(envCode);
            if (org.springframework.util.StringUtils.isEmpty(nginxProxyPassTail)) {
                result += "    proxy_pass http://upstream." + envName + nginxUpstreamName + ".java;\n";
            } else {
                result += "    proxy_pass http://upstream." + envName + nginxUpstreamName + ".java" + nginxProxyPassTail + " ;\n";
            }
        }
        result += "    include /usr/local/nginx/conf/vhost/proxy_default.conf;\n";
        result += "}\n\n";
        projects.add(serverGroupDO.getName());
        return result;
    }


}
