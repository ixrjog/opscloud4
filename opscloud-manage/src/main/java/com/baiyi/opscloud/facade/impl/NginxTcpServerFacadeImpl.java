package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.bo.TcpServerBO;
import com.baiyi.opscloud.common.util.BeetlUtils;
import com.baiyi.opscloud.common.util.IOUtils;
import com.baiyi.opscloud.config.OpscloudConfig;
import com.baiyi.opscloud.domain.generator.opscloud.OcDubboMappingServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcDubboTcpMapping;
import com.baiyi.opscloud.domain.generator.opscloud.OcFileTemplate;
import com.baiyi.opscloud.facade.NginxTcpServerFacade;
import com.baiyi.opscloud.service.dubbo.OcDubboMappingServerService;
import com.baiyi.opscloud.service.dubbo.OcDubboTcpMappingService;
import com.baiyi.opscloud.service.file.OcFileTemplateService;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/10/19 10:25 上午
 * @Version 1.0
 */
@Slf4j
@Service
public class NginxTcpServerFacadeImpl implements NginxTcpServerFacade {

    @Resource
    private OcDubboMappingServerService ocDubboMappingServerService;

    @Resource
    private OcDubboTcpMappingService ocDubboTcpMappingService;

    @Resource
    private OcFileTemplateService ocFileTemplateService;

    @Resource
    private OpscloudConfig opscloudConfig;

    private static final String FILE_TEMPLATE_NGINX_TCP_DUBBO = "NGINX_TCP_DUBBO";

    private static final String FILE_TEMPLATE_NGINX_TCP_DEBUG = "NGINX_TCP_DEBUG";

    private static final String NGINX_TCP_DUBBO_PATH = "nginx/tcp/dubbo" ;

    private static final int DEFAULT = 0;

    @Override
    public String buildNginxDubboTcpServerByEnv(String env) {
        OcFileTemplate nginxTcpDubboTemplate = ocFileTemplateService.queryOcFileTemplateByUniqueKey(FILE_TEMPLATE_NGINX_TCP_DUBBO, DEFAULT);
        OcFileTemplate nginxTcpDebugTemplate = ocFileTemplateService.queryOcFileTemplateByUniqueKey(FILE_TEMPLATE_NGINX_TCP_DEBUG, DEFAULT);
        List<OcDubboTcpMapping> ocDubboTcpMappings = ocDubboTcpMappingService.queryOcDubboTcpMappingByEnv(env);
        StringBuilder body = new StringBuilder();
        ocDubboTcpMappings.forEach(e -> {
            List<TcpServerBO> servers = convert(ocDubboMappingServerService.queryOcDubboMappingServerByTcpMappingId(e.getId()));
            if (!CollectionUtils.isEmpty(servers)) {
                body.append(buildNginxTcpDubbo(e, env, nginxTcpDubboTemplate.getContent(), servers));

                body.append(buildNginxTcpDebug(e, env, nginxTcpDebugTemplate.getContent(), servers));
            }
        });
        return body.toString();
    }

    @Override
    public void writeNginxDubboTcpServerConfByEnv(String env) {
        String conf = buildNginxDubboTcpServerByEnv(env);
        String fileName = Joiner.on("").join("tcp_dubbo-", env, ".conf");
        String path = Joiner.on("/").join(opscloudConfig.getDataPath(), NGINX_TCP_DUBBO_PATH, fileName);
        IOUtils.writeFile(conf, path);
    }


    private String buildNginxTcpDubbo(OcDubboTcpMapping ocDubboTcpMapping, String env, String templateContent, List<TcpServerBO> servers) {
        Map<String, Object> contentMap = Maps.newHashMap();
        contentMap.put("name", ocDubboTcpMapping.getName());
        contentMap.put("envName", env);
        contentMap.put("tcpType", "dubbo");
        contentMap.put("tcpPort", ocDubboTcpMapping.getTcpPort().toString());
        contentMap.put("servers", servers);
        try {
            return BeetlUtils.renderTemplate(templateContent, contentMap);
        } catch (IOException ioe) {
            return "# " + ocDubboTcpMapping.getName() + "render template error\n";
        }
    }

    private String buildNginxTcpDebug(OcDubboTcpMapping ocDubboTcpMapping, String env, String templateContent, List<TcpServerBO> servers) {
        Map<String, Object> contentMap = Maps.newHashMap();
        contentMap.put("name", ocDubboTcpMapping.getName());
        contentMap.put("envName", env);
        contentMap.put("tcpType", "debug");
        contentMap.put("tcpPort", ocDubboTcpMapping.getTcpPort() + 10000);
        contentMap.put("servers", servers);
        try {
            return BeetlUtils.renderTemplate(templateContent, contentMap);
        } catch (IOException ioe) {
            return "# " + ocDubboTcpMapping.getName() + "render template error\n";
        }
    }

    private List<TcpServerBO> convert(List<OcDubboMappingServer> servers) {
        Map<String, TcpServerBO> tcpServerMap = Maps.newHashMap();
        servers.forEach(s -> {
            TcpServerBO tcpServerBO = TcpServerBO.builder()
                    .ip(s.getIp())
                    .port(s.getDubboPort().toString())
                    .build();
            tcpServerMap.put(tcpServerBO.getIp(), tcpServerBO);
        });
        return Lists.newArrayList(tcpServerMap.values());
    }


}
