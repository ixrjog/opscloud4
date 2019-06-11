package com.sdg.cmdb.service.configurationProcessor;

import com.sdg.cmdb.dao.cmdb.KubernetesDao;
import com.sdg.cmdb.domain.kubernetes.KubernetesServiceCluster;
import com.sdg.cmdb.domain.kubernetes.KubernetesServiceDO;
import com.sdg.cmdb.domain.nginx.NginxTcpDO;
import com.sdg.cmdb.domain.nginx.NginxTcpDubboDO;
import com.sdg.cmdb.domain.nginx.NginxTcpVO;
import com.sdg.cmdb.domain.server.EnvType;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.service.CacheKeyService;
import com.sdg.cmdb.service.KubernetesService;
import com.sdg.cmdb.service.impl.ConfigServerGroupServiceImpl;
import com.sdg.cmdb.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class NginxTcpProcessorService extends ConfigurationProcessorAbs {
    public static final String CACHE_KEY = "NginxTcpProcessorService:";

    public static final String DUBBO_RESOLVE_FILE = "dubbo-resolve.properties";

    @Value("#{cmdb['dubbo.resolve.path']}")
    private String dubboResolvePath;


    @Autowired
    private CacheKeyService cacheKeyService;

    @Autowired
    private KubernetesService kubernetesService;

    @Autowired
    private KubernetesDao kubernetesDao;

    private String getTcpDubboFileCacheKey(String clusterKey) {
        return CACHE_KEY + "dubbo:clusterKey:" + clusterKey;
    }

    private String getTcpDubboFileByCache(String clusterKey) {
        String key = getTcpDubboFileCacheKey(clusterKey);
        return cacheKeyService.getKeyByString(key);
    }


    public void cleanTcpDubboCache(String clusterKey){
        cacheKeyService.del(getTcpDubboFileCacheKey(clusterKey));
    }


    public String getTcpDubboFile(String clusterKey) {
        String cache = getTcpDubboFileByCache(clusterKey);
        if (!StringUtils.isEmpty(cache)) return cache;
        List<NginxTcpDubboDO> list = nginxDao.queryNginxTcpDubbo(clusterKey);
        HashMap<Integer, List<NginxTcpDubboDO>> map = new HashMap<>();
        for (NginxTcpDubboDO tcpDubboDO : list) {
            List<NginxTcpDubboDO> dubbos;
            if (map.containsKey(tcpDubboDO.getServicePort())) {
                dubbos = map.get(tcpDubboDO.getServicePort());
            } else {
                dubbos = new ArrayList<>();
            }
            dubbos.add(tcpDubboDO);
            map.put(tcpDubboDO.getServicePort(), dubbos);
        }

        String dubboFile = getHeadInfo();
        // key serviceId
       // HashMap<Long, ServerGroupDO> groupMap = new HashMap<>();
        for (Integer port : map.keySet()) {
            List<NginxTcpDubboDO> tcpDubboList = map.get(port);
            dubboFile += getDubbodAPI(tcpDubboList);
        }

        cacheKeyService.set(getTcpDubboFileCacheKey(clusterKey), dubboFile, 30);
        IOUtils.writeFile(dubboFile, dubboResolvePath + "/" + DUBBO_RESOLVE_FILE);
        return dubboFile;

    }


    private String getDubbodAPI(List<NginxTcpDubboDO> list) {
        ServerGroupDO serverGroupDO = null;
        String body = "";
        for (NginxTcpDubboDO nginxTcpDubboDO : list) {
            if (serverGroupDO == null) {
                KubernetesServiceDO kubernetesServiceDO = kubernetesDao.getServiceById(nginxTcpDubboDO.getKubernetesServiceId());
                serverGroupDO = serverGroupDao.queryServerGroupById(kubernetesServiceDO.getServerGroupId());
            }
            body += nginxTcpDubboDO.getDubbo() + "=dubbo://" + NginxTcpVO.DomainEnum.test.getDesc() + ":" + nginxTcpDubboDO.getServicePort() + "\n";
        }
        String head = "";
        if (serverGroupDO != null) {
            head = "# " + serverGroupDO.getName() + "\n";
            if (!StringUtils.isEmpty(serverGroupDO.getContent()))
                head += "# " + serverGroupDO.getContent() + "\n";
            head += "\n";
        }
        return head + body + "\n\n";
    }


    public String getTcpFile(KubernetesServiceDO kubernetesServiceDO, NginxTcpDO nginxTcpDO) {
        return getTcpFile(kubernetesServiceDO, nginxTcpDO.getEnvType(), nginxTcpDO.getPortName());
    }

    public String getTcpFile(KubernetesServiceDO kubernetesServiceDO, int envType, String portName) {
        //   KubernetesServiceCluster getServerList(long serverGroupId, int env, String portName, int size);
        if (kubernetesServiceDO == null || kubernetesServiceDO.getServerGroupId() == 0) {
            log.error("kubernetesServiceDO = null");
            return "";
        }
        KubernetesServiceCluster serviceCluster = kubernetesService.getServerList(kubernetesServiceDO.getServerGroupId(), envType, portName, 4);
        if (serviceCluster == null || serviceCluster.getServerList().size() == 0)
            return "";
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(kubernetesServiceDO.getServerGroupId());

        String upstreamName = getUpstreamName(kubernetesServiceDO, portName, envType);
        //String tcpFile = getHeadInfo();
        String tcpFile = "# " + serverGroupDO.getName() + "/" + kubernetesServiceDO.getName() + "\n";
        tcpFile += getTcpUpstream(serviceCluster, upstreamName);
        tcpFile += "\n";
        tcpFile += getTcpServer(serviceCluster, upstreamName);
        return tcpFile;
    }

    private String getTcpUpstream(KubernetesServiceCluster serviceCluster, String upstreamName) {
        String indent = ConfigServerGroupServiceImpl.nginx_locaton_indent;
        String upstreamHead = indent + "upstream " + upstreamName + " {\n";
        String tcpUpstream = upstreamHead;
        tcpUpstream += getTcpUpstreamServerList(serviceCluster) + "\n";
        //tcpUpstream += indent + indent + "check interval=3000 rise=2 fall=5 timeout=1000;\n";
        tcpUpstream += indent + "}\n";
        return tcpUpstream;
    }

    private String getTcpUpstreamServerList(KubernetesServiceCluster serviceCluster) {
        String indent = ConfigServerGroupServiceImpl.nginx_locaton_indent;
        String serverList = "";
        for (ServerDO serverDO : serviceCluster.getServerList())
            serverList += indent + indent + "server " + serverDO.getInsideIp() + ":" + serviceCluster.getNodePort() + ";\n";
        return serverList;
    }

    private String getTcpServer(KubernetesServiceCluster serviceCluster, String upstreamName) {
        String indent = ConfigServerGroupServiceImpl.nginx_locaton_indent;
        String tcpServer = indent + "server {\n";
        tcpServer += indent + indent + "listen " + serviceCluster.getNodePort() + ";\n\n";
        tcpServer += indent + indent + "proxy_pass " + upstreamName + ";\n";
        tcpServer += indent + "}\n";
        return tcpServer;
    }

    private String getUpstreamName(KubernetesServiceDO kubernetesServiceDO, String portName, int envType) {
        String envName = EnvType.EnvTypeEnum.getEnvTypeName(envType);
        return "upstream." + envName + '.' + kubernetesServiceDO.getName() + "." + portName;
    }


}
