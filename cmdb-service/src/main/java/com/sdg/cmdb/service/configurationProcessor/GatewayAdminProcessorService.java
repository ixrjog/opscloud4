package com.sdg.cmdb.service.configurationProcessor;


import com.sdg.cmdb.domain.gatewayAdmin.AppServerSet;
import com.sdg.cmdb.domain.gatewayAdmin.AppSet;
import com.sdg.cmdb.domain.kubernetes.KubernetesServiceCluster;
import com.sdg.cmdb.domain.gatewayAdmin.ServerConfigItem;
import com.sdg.cmdb.domain.server.EnvType;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.service.CacheKeyService;
import com.sdg.cmdb.service.KubernetesService;
import com.sdg.cmdb.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class GatewayAdminProcessorService extends ConfigurationProcessorAbs {
    public static final String CACHE_KEY = "GatewayAdminProcessorService:";

    @Autowired
    private CacheKeyService cacheKeyService;

    @Autowired
    private KubernetesService kubernetesService;

    @Autowired
    private ServerService serverService;

    public static final String PORT_NAME = "http";
    public static final int SERVER_CLUSTER_SIZE = 4;

    public static final String GETWAYADMIN_APP_SERVICE_TEST = "GETWAYADMIN_APP_SERVICE_TEST";
    public static final String GETWAYADMIN_APP_SERVICE_PRE = "GETWAYADMIN_APP_SERVICE_PRE";

    public AppSet getAppSet(long serverGroupId) {
        ServerGroupDO serverGroupDO = new ServerGroupDO(serverGroupId);
        String appName = configServerGroupService.queryGatewayAdminAppName(serverGroupDO);
        if (StringUtils.isEmpty(appName))
            return null;
        String routePath = configServerGroupService.queryGatewayAdminRoutePath(serverGroupDO);
        AppSet appSet = new AppSet(appName, routePath);
        return appSet;
    }

    public AppServerSet getAppServerSet(long serverGroupId, int envType) {
        ServerGroupDO serverGroupDO = new ServerGroupDO(serverGroupId);
        String appName = configServerGroupService.queryGatewayAdminAppName(serverGroupDO);
        String healthCheckPath = configServerGroupService.queryGatewayAdminAppHealthCheckPath(serverGroupDO);
        if (StringUtils.isEmpty(appName))
            return null;
        if (envType == EnvType.EnvTypeEnum.prod.getCode()) {
            List<ServerDO> serverList = serverService.getServerByGroup(serverGroupDO, envType);
            List<ServerConfigItem> serverConfigItems = new ArrayList<>();
            for (ServerDO serverDO : serverList) {
                String appPort = configServerGroupService.queryGatewayAdminAppPort(serverGroupDO);
                serverConfigItems.add(new ServerConfigItem(serverDO, appPort));
            }
            AppServerSet appServerSet = new AppServerSet(appName, healthCheckPath, serverConfigItems);
            return appServerSet;
        }
        return getAppServerByKubernetes(appName, healthCheckPath, serverGroupId, envType);
//        if (envType == EnvType.EnvTypeEnum.test.getCode()) {
//            return getAppServerSet(appName, healthCheckPath, serverGroupDO, envType, GETWAYADMIN_APP_SERVICE_TEST);
//        }
//        if (envType == EnvType.EnvTypeEnum.pre.getCode()) {
//            return getAppServerSet(appName, healthCheckPath, serverGroupDO, envType, GETWAYADMIN_APP_SERVICE_PRE);
//        }
//        return null;
    }


    private AppServerSet getAppServerByKubernetes(String appName, String healthCheckPath, long serverGroupId, int env) {
        ServerGroupDO serverGroupDO = new ServerGroupDO(serverGroupId);
        //  AppSetKubernetes getServerList(long serverGroupId, int env, String portName, int size);
        KubernetesServiceCluster appSetKubernetes = kubernetesService.getServerList(serverGroupId, env, PORT_NAME, SERVER_CLUSTER_SIZE);
        if (appSetKubernetes.getServerList() != null && appSetKubernetes.getServerList().size() != 0) {
            List<ServerConfigItem> serverConfigItems = new ArrayList<>();
            for (ServerDO serverDO : appSetKubernetes.getServerList()) {
                serverConfigItems.add(new ServerConfigItem(serverDO, appSetKubernetes.getNodePort()));
            }
            AppServerSet appServerSet = new AppServerSet(appName, healthCheckPath, serverConfigItems);
            return appServerSet;
        }
        if (env == EnvType.EnvTypeEnum.test.getCode()) {
            return getAppServerSet(appName, healthCheckPath, serverGroupDO, env, GETWAYADMIN_APP_SERVICE_TEST);
        }
        if (env == EnvType.EnvTypeEnum.pre.getCode()) {
            return getAppServerSet(appName, healthCheckPath, serverGroupDO, env, GETWAYADMIN_APP_SERVICE_PRE);
        }
        return null;
    }


    private AppServerSet getAppServerSet(String appName, String healthCheckPath, ServerGroupDO serverGroupDO, int envType, String serviceKey) {
        String appService = configServerGroupService.queryGatewayAdminAppServiceByKey(serverGroupDO, serviceKey);
        List<ServerConfigItem> serverConfigItems = new ArrayList<>();
        ServerConfigItem serverConfigItem = new ServerConfigItem(appService, appName + "-" + EnvType.EnvTypeEnum.getEnvTypeName(envType));
        serverConfigItems.add(serverConfigItem);
        AppServerSet appServerSet = new AppServerSet(appName, healthCheckPath, serverConfigItems);
        return appServerSet;
    }

}
