package com.sdg.cmdb.service.configurationProcessor;

import com.sdg.cmdb.domain.nginx.*;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.service.CacheKeyService;
import com.sdg.cmdb.service.impl.ConfigServerGroupServiceImpl;
import com.sdg.cmdb.util.PathUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NginxFileProcessorService extends ConfigurationProcessorAbs {

    public static final String CACHE_KEY = "NginxFileProcessorService:";

    @Autowired
    private CacheKeyService cacheKeyService;

    /**
     * NginxFileProcessorService:upstreamName:upstream.zebra-platform.java
     *
     * @param serverGroupDO
     * @param envCode
     * @return
     */
    private String getCacheKey(ServerGroupDO serverGroupDO, int envCode) {
        return CACHE_KEY + "upstreamName:" + getUpstramName(serverGroupDO, envCode);
    }

    public void delCache(ServerDO serverDO) {
        delCache(new ServerGroupDO(serverDO.getServerGroupId()),serverDO.getEnvType());
    }

    public void delCache(ServerGroupDO serverGroupDO) {
        for (ServerDO.EnvTypeEnum e : ServerDO.EnvTypeEnum.values())
            delCache(serverGroupDO, e.getCode());
    }

    public void delCache(ServerGroupDO serverGroupDO, int envCode) {
        String cacheKey = getCacheKey(serverGroupDO, envCode);
        cacheKeyService.del(cacheKey);
    }


    private boolean isBuildLocation(ServerGroupDO serverGroupDO) {
        return configServerGroupService.isbuildLocation(serverGroupDO);
    }


    /**
     * 取文件内容
     *
     * @param vhostDO
     * @param vhostEnvDO
     * @param envFileDO
     * @return
     */
    public String getFile(VhostDO vhostDO, VhostEnvDO vhostEnvDO, EnvFileDO envFileDO) {
        String nginxConf = getHead(vhostDO, vhostEnvDO, envFileDO);
        if (envFileDO.getFileType() == EnvFileDO.FileTypeEnum.location.getCode()) {
            nginxConf += buildLocation(vhostDO.getId(), vhostEnvDO.getEnvType());
        }

        if (envFileDO.getFileType() == EnvFileDO.FileTypeEnum.upstream.getCode()) {
            nginxConf += buildUpstream(vhostDO.getId(), vhostEnvDO.getEnvType());
        }

        return nginxConf;
    }

    public void invokeFile(NginxFile nginxFile) {
        String nginxConf = getFile(nginxFile.getVhostDO(), nginxFile.getVhostEnvDO(), nginxFile.getEnvFileDO());
        nginxFile.setFile(nginxConf);
    }

    /**
     * 取文件路径
     *
     * @param vhostEnvDO
     * @param envFileDO
     * @return
     */
    public String getFilePath(VhostEnvDO vhostEnvDO, EnvFileDO envFileDO) {
        return PathUtils.getPath(vhostEnvDO.getConfPath(), envFileDO.getFilePath(), envFileDO.getFileName());
    }

    private String getHead(VhostDO vhostDO, VhostEnvDO vhostEnvDO, EnvFileDO envFileDO) {
        String headInfo = getHeadInfo();

        // 域名信息
        headInfo += "# " + vhostDO.getServerName() + "<" + vhostDO.getServerKey() + ">\n";
        if (!StringUtils.isEmpty(vhostDO.getContent()))
            headInfo += "# " + vhostDO.getContent() + "\n";
        // 环境信息
        headInfo += "# 环境 : " +
                ServerDO.EnvTypeEnum.getEnvTypeName(vhostEnvDO.getEnvType()) + "\n";

        headInfo += "# 配置文件路径 : " + getFilePath(vhostEnvDO, envFileDO) + "\n";
        headInfo += "# 配置文件Key : " + envFileDO.getFileKey() + "\n\n";

        return headInfo;
    }


    /**
     * 获取location中的扩展参数并格式化
     *
     * @param serverGroupDO
     * @return
     */
    private String acqLocationParam(ServerGroupDO serverGroupDO) {

        String sourceParams = configServerGroupService.queryNginxLocationParam(serverGroupDO);
        if (org.apache.commons.lang3.StringUtils.isEmpty(sourceParams)) return "";

        String[] params = sourceParams.split(";");
        String indent = ConfigServerGroupServiceImpl.nginx_locaton_indent;
        String result = indent + "# " + ConfigServerGroupServiceImpl.nginx_location_param + " = " + sourceParams + "\n";

        for (String param : params) {
            result += ConfigServerGroupServiceImpl.nginx_locaton_indent + param + ";\n";
        }
        return result;
    }

    private String buildLocation(long vhostId, int envType) {
        String result = "";
        List<ServerGroupDO> listServerGroupDO = getServerGroup(vhostId);
        for (ServerGroupDO serverGroupDO : listServerGroupDO) {
            List<ServerDO> listServerDO = acqServerByGroup(serverGroupDO, envType);
            // 无服务器
            if (listServerDO.size() == 0)
                continue;
            // NGINX_LOCATION_MANAGE_BUILD = false
            if (!isBuildLocation(serverGroupDO)) continue;
            String l = buildLocation(serverGroupDO, envType);
            if (l != null)
                result += l + "\n";
        }
        return result;
    }

    /**
     * 查询vhost的服务器组
     *
     * @param vhostId
     * @return
     */
    private List<ServerGroupDO> getServerGroup(long vhostId) {
        List<VhostServerGroupDO> vhostServerGroups = nginxDao.queryVhostServerGroupByVhostId(vhostId);
        List<ServerGroupDO> list = new ArrayList<>();
        for (VhostServerGroupDO vhostServerGroupDO : vhostServerGroups) {
            ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(vhostServerGroupDO.getServerGroupId());
            list.add(serverGroupDO);
        }
        return list;
    }


    public String buildLocation(ServerGroupDO serverGroupDO, int envType) {
        List<ServerDO> listServerDO = acqServerByGroup(serverGroupDO, envType);
        // 无服务器
        if (listServerDO.size() == 0)
            return "";
        if (!isBuildLocation(serverGroupDO)) return "";

        String projectName = configServerGroupService.queryProjectName(serverGroupDO);
        if (projectName == null) return null;
        String result;
        if (serverGroupDO.getContent() != null && !serverGroupDO.getContent().isEmpty()) {
            result = "# " + serverGroupDO.getContent() + "\n";
        } else {
            result = "# " + serverGroupDO.getName() + "\n";
        }
        result += "location " + configServerGroupService.getNginxLocation(serverGroupDO) + " {\n";
        // 增加location扩展参数
        result += acqLocationParam(serverGroupDO);

        String nginxLocationLimitReq = configServerGroupService.queryNginxLocationLimitReq(serverGroupDO);
        String indent = ConfigServerGroupServiceImpl.nginx_locaton_indent;
        if (nginxLocationLimitReq != null)
            result += indent + nginxLocationLimitReq + "\n";
        result += configServerGroupService.queryNginxProxyPass(serverGroupDO, envType);
        result += "}\n\n";
        return result;
    }

    //////////////


    public String buildUpstream(long vhostId, int envType) {
        //List<String> projects = new ArrayList<String>();
        String upstream = "";
        List<ServerGroupDO> listServerGroupDO = getServerGroup(vhostId);
        for (ServerGroupDO serverGroupDO : listServerGroupDO) {
            String projectName = configServerGroupService.queryProjectName(serverGroupDO);
            if (projectName == null || projectName.isEmpty()) continue;
            List<ServerDO> listServerDO = new ArrayList<ServerDO>();
            // TODO 这里判断缓存
            String cacheKey = getCacheKey(serverGroupDO, envType);
            String cacheConfig = cacheKeyService.getKeyByString(cacheKey);
            if (!org.apache.commons.lang3.StringUtils.isEmpty(cacheConfig)) {
                upstream += cacheConfig;
                continue;
            }
            listServerDO = acqServerByGroup(serverGroupDO, envType);

            if (listServerDO.size() == 0) continue;
            String u = buildUpstream(serverGroupDO, listServerDO, envType);
            if (u != null)
                upstream += u;
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
    private String getUpsteamCheck(ServerGroupDO serverGroupDO) {
        String indent = ConfigServerGroupServiceImpl.nginx_locaton_indent;
        String indentX2 = indent + indent;
        String result = indentX2 + "check interval=1000 rise=1 fall=3 timeout=1000 type=http;\n";
        return result + indentX2 + "check_http_send \"GET " + configServerGroupService.queryHttpCheck(serverGroupDO) + " HTTP/1.0\\r\\n\\r\\n\";\n";

    }


    /**
     * 做缓存
     *
     * @param serverGroupDO
     * @param listServerDO
     * @param envCode
     * @return
     */
    public String buildUpstream(ServerGroupDO serverGroupDO, List<ServerDO> listServerDO, int envCode) {
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
        upstream += indent + getUpstramName(serverGroupDO, envCode) + " { \n";
        for (ServerDO serverDO : listServerDO) {
            upstream += indentX2 + "# " + ServerDO.EnvTypeEnum.getEnvTypeName(serverDO.getEnvType()) + "-" + serverDO.getSerialNumber() + "\n";
            upstream += acqUpstreamServer(serverGroupDO, serverDO);
        }
        if (listServerDO.size() > 1 && configServerGroupService.isBuildNginxCheck(serverGroupDO))
            upstream += getUpsteamCheck(serverGroupDO);
        upstream += "    }\n\n";
        // TODO 缓存
        String cacheKey = getCacheKey(serverGroupDO, envCode);
        cacheKeyService.set(cacheKey, upstream, 10080);
        return upstream;
    }

    /**
     * upstream.zebra-platform.java
     * upstream.gray.zebra-platform.java
     *
     * @param serverGroupDO
     * @param envCode
     * @return
     */
    private String getUpstramName(ServerGroupDO serverGroupDO, int envCode) {
        String nginxUpstreamName = configServerGroupService.queryNginxUpstreamName(serverGroupDO);
        if (envCode == ServerDO.EnvTypeEnum.prod.getCode()) {
            return "upstream upstream." + nginxUpstreamName + ".java";
        } else {
            return "upstream upstream." + ServerDO.EnvTypeEnum.getEnvTypeName(envCode) + '.' + nginxUpstreamName + ".java";
        }
    }


    private String acqUpstreamServer(ServerGroupDO serverGroupDO, ServerDO serverDO) {
        String indent = ConfigServerGroupServiceImpl.nginx_locaton_indent;
        String indentX2 = indent + indent;
        String tomcatHttpPort = configServerGroupService.queryUpstreamPort(serverGroupDO);

        String weight = configServerGroupService.queryNginxUpstreamWeight(serverDO);
        if (!org.springframework.util.StringUtils.isEmpty(weight) && !weight.equals("1"))
            weight = " weight=" + weight;

        String serverType = configServerGroupService.queryNginxUpstreamServerType(serverDO);
        String maxFails = "";
        String failTimeout = "";
        if (!org.springframework.util.StringUtils.isEmpty(serverType)) {
            if (serverType.equalsIgnoreCase("down"))
                serverType = " down";
            if (serverType.equalsIgnoreCase("backup"))
                serverType = " backup";
        } else {
            //参数不能同时拥有
            maxFails = configServerGroupService.queryNginxUpstreamMaxFails(serverGroupDO);
            if (!org.springframework.util.StringUtils.isEmpty(maxFails))
                maxFails = " max_fails=" + maxFails;

            failTimeout = configServerGroupService.queryNginxUpstreamFailTimeout(serverGroupDO);
            if (!org.springframework.util.StringUtils.isEmpty(maxFails))
                failTimeout = " fail_timeout=" + failTimeout;
        }
        String result = indentX2 + "server " + serverDO.getInsideIp() + ":" + tomcatHttpPort + weight + serverType + maxFails + failTimeout + ";\n";
        return result;
    }


}
