package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.bo.DubboMappingServerBO;
import com.baiyi.opscloud.bo.DubboTcpMappingBO;
import com.baiyi.opscloud.builder.DubboMappingBuilder;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.CreatedByUtils;
import com.baiyi.opscloud.common.util.IOUtils;
import com.baiyi.opscloud.config.OpscloudConfig;
import com.baiyi.opscloud.domain.generator.opscloud.OcDubboMapping;
import com.baiyi.opscloud.domain.generator.opscloud.OcDubboMappingServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcDubboTcpMapping;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.dubbo.ZookeeperServer;
import com.baiyi.opscloud.facade.DubboMappingFacade;
import com.baiyi.opscloud.module.DubboTcpMappingModule;
import com.baiyi.opscloud.service.dubbo.OcDubboMappingServerService;
import com.baiyi.opscloud.service.dubbo.OcDubboMappingService;
import com.baiyi.opscloud.service.dubbo.OcDubboTcpMappingService;
import com.baiyi.opscloud.service.server.OcServerService;
import com.baiyi.opscloud.task.util.TaskUtil;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.google.common.base.Joiner;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.baiyi.opscloud.common.base.Global.ASYNC_POOL_TASK_COMMON;
import static com.baiyi.opscloud.task.DubboMappingTask.TASK_SERVER_DUBBO_MAPPING_TOPIC;

/**
 * @Author baiyi
 * @Date 2020/10/9 2:55 下午
 * @Version 1.0
 */
@Component("DubboMappingFacade")
public class DubboMappingFacadeImpl implements DubboMappingFacade {

    @Resource
    private ZookeeperServer zookeeperServer;

    @Resource
    private OcServerService ocServerService;

    @Resource
    private OcDubboMappingService ocDubboMappingService;

    @Resource
    private OcDubboMappingServerService ocDubboMappingServerService;

    @Resource
    private OcDubboTcpMappingService ocDubboTcpMappingService;

    @Resource
    private OpscloudConfig opscloudConfig;

    @Resource
    private TaskUtil taskUtil;

    private static final String DUBBO = "/dubbo";

    private static final String PROVIDERS = "providers";

    private static final String TCP_SERVER = "tcp-test.xinc818.com";

    private static final String DUBBO_RESOLVE_PROPERTIES = "dubbo-resolve.properties";

    private static final int TCP_DEFAULT_PORT = 20000;

    private List<String> getDubboList(String env) {
        List<String> list = zookeeperServer.getNodeChild(env, DUBBO);
        zookeeperServer.closeClient(env);
        return list;
    }

    @Override
    @Async(value = ASYNC_POOL_TASK_COMMON)
    public void refreshDubboProviderByEnv(String env) {
        syncDubboProviderByEnv(env);
        bindDubboMappingPortByEnv(env);
        writeDubboResolvePropertiesByEnv(env);
        // 发送消息
        taskUtil.sendMessage(TASK_SERVER_DUBBO_MAPPING_TOPIC);
    }

    @Override
    public void syncDubboProviderByEnv(String env) {
        List<String> dubboList = getDubboList(env);
        dubboList.forEach(dubbo -> {
            String providerPath = Joiner.on("/").join(DUBBO, dubbo, PROVIDERS);
            List<String> providerList = zookeeperServer.getNodeChild(env, providerPath);
            zookeeperServer.closeClient(env);
            if (!CollectionUtils.isEmpty(providerList))
                providerList.forEach(provider -> saveDubboProvider(env, provider));
        });
    }

    private void saveDubboProvider(String env, String provider) {
        try {
            String url = URLDecoder.decode(provider, "UTF-8");
            url = url.replace("dubbo://", "http://");
            URL u = new URL(url);
            List<NameValuePair> pairList = URLEncodedUtils.parse(url, StandardCharsets.UTF_8);
            String ip = u.getHost();
            // 过滤掉vpn，办公网ip
            if (!ip.startsWith("172.16")) return;

            OcDubboMapping pre = DubboMappingBuilder.build(env, pairList);
            OcDubboMapping ocDubboMapping = ocDubboMappingService.qeuryOcDubboMappingByUniqueKey(pre);

            if (ocDubboMapping == null) {
                ocDubboMappingService.addOcDubboMapping(pre);
                saveDubboMappingServer(pre, ip, u.getPort());
            } else {
                saveDubboMappingServer(ocDubboMapping, ip, u.getPort());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveDubboMappingServer(OcDubboMapping ocDubboMapping, String ip, int port) {
        OcServer ocServer = ocServerService.queryOcServerByPrivateIp(ip);
        DubboMappingServerBO bo = DubboMappingServerBO.builder()
                .mappingId(ocDubboMapping.getId())
                .ip(ip)
                .dubboPort(port)
                .serverId(ocServer.getId())
                .serverName(ocServer.getName())
                .build();
        OcDubboMappingServer ocDubboMappingServer = BeanCopierUtils.copyProperties(bo, OcDubboMappingServer.class);
        if (ocDubboMappingServerService.queryOcDubboMappingServerByUniqueKey(ocDubboMappingServer) == null)
            ocDubboMappingServerService.addOcDubboMappingServer(ocDubboMappingServer);
    }

    @Override
    public void bindDubboMappingPortByEnv(String env) {
        while (true) {
            OcDubboMapping ocDubboMapping = ocDubboMappingService.queryOneOcDubboUnmappedByEnv(env);
            if (ocDubboMapping == null) break;
            // 查询接口下的服务器
            List<OcDubboMappingServer> servers = ocDubboMappingServerService.queryOcDubboMappingServerByMappingId(ocDubboMapping.getId());
            // 需要绑定的服务器
            for (OcDubboMappingServer server : servers) {
                if (server.getTcpMappingId() == 0) {
                    OcDubboMappingServer ocDubboMappingServer = ocDubboMappingServerService.queryOneBindOcDubboMappingServer(server.getIp());
                    if (ocDubboMappingServer != null) {
                        ocDubboMapping.setTcpMappingId(ocDubboMappingServer.getTcpMappingId());
                    } else {
                        OcDubboTcpMapping ocDubboTcpMapping = acqNewDubboTcpMapping(server, ocDubboMapping);
                        ocDubboMapping.setTcpMappingId(ocDubboTcpMapping.getId());
                    }
                    ocDubboMappingService.updateOcDubboMapping(ocDubboMapping);
                    mappingServers(server.getIp(), ocDubboMapping.getTcpMappingId());
                }
            }
        }
        // 绑定新增的服务器
        bindDubboMappingServer();
    }

    private void bindDubboMappingServer() {
        List<OcDubboMappingServer> servers = ocDubboMappingServerService.queryOcDubboMappingServerUnmapped();
        if (CollectionUtils.isEmpty(servers))
            return;
        servers.forEach(s -> {
            OcDubboMapping ocDubboMapping = ocDubboMappingService.queryOcDubboMappingById(s.getMappingId());
            if (ocDubboMapping.getTcpMappingId() == 0) return;
            s.setTcpMappingId(ocDubboMapping.getTcpMappingId());
            ocDubboMappingServerService.updateOcDubboMappingServer(s);
        });
    }


    private OcDubboTcpMapping acqNewDubboTcpMapping(OcDubboMappingServer server, OcDubboMapping ocDubboMapping) {
        DubboTcpMappingBO bo = DubboTcpMappingBO.builder()
                .env(ocDubboMapping.getEnv())
                .name(server.getServerName())
                .build();

        OcDubboTcpMapping pre = BeanCopierUtils.copyProperties(bo, OcDubboTcpMapping.class);
        OcDubboTcpMapping ocDubboTcpMapping = ocDubboTcpMappingService.queryOcDubboTcpMappingByUniqueKey(pre);
        if (ocDubboTcpMapping != null)
            return ocDubboTcpMapping;
        ocDubboTcpMapping = ocDubboTcpMappingService.queryOcDubboTcpMappingByMaxPort();
        if (ocDubboTcpMapping == null) {
            pre.setTcpPort(TCP_DEFAULT_PORT);
        } else {
            pre.setTcpPort(ocDubboTcpMapping.getTcpPort() + 1);
        }
        ocDubboTcpMappingService.addOcDubboTcpMapping(pre);
        return pre;
    }

    private void mappingServers(String ip, int tcpMappingId) {
        List<OcDubboMappingServer> unmappedServers = ocDubboMappingServerService.queryOcDubboMappingServerByIpAndIsMapping(ip, false);
        unmappedServers.forEach(e -> {
            e.setTcpMappingId(tcpMappingId);
            ocDubboMappingServerService.updateOcDubboMappingServer(e);
            OcDubboMapping ocDubboMapping = ocDubboMappingService.queryOcDubboMappingById(e.getMappingId());
            if (ocDubboMapping.getTcpMappingId() == 0) {
                ocDubboMapping.setTcpMappingId(tcpMappingId);
                ocDubboMappingService.updateOcDubboMapping(ocDubboMapping);
            }
        });
    }

    @Override
    public void writeDubboResolvePropertiesByEnv(String env) {
        String properties = buildDubboResolveByEnv(env);
        String path = Joiner.on("/").join(opscloudConfig.getDataPath(), "res", "static", env, DUBBO_RESOLVE_PROPERTIES);
        IOUtils.writeFile(properties, path);
    }

    @Override
    public String buildDubboResolveByEnv(String env) {
        List<OcDubboTcpMapping> tcpMappings = ocDubboTcpMappingService.queryOcDubboTcpMappingByEnv(env);
        StringBuilder body = new StringBuilder();
        body.append(CreatedByUtils.getHead());

        tcpMappings.forEach(e -> {
            DubboTcpMappingModule module = DubboTcpMappingModule.builder()
                    .ocDubboTcpMapping(e)
                    .ocDubboMappings(ocDubboMappingService.queryOcDubboMappingByTcpMappingId(e.getId()))
                    .build();
            body.append(module.buildProperties(TCP_SERVER));
        });
        return body.toString();
    }

}
