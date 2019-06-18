package com.sdg.cmdb.service.impl;


import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sdg.cmdb.dao.cmdb.KubernetesDao;
import com.sdg.cmdb.dao.cmdb.NginxDao;
import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.dubbo.DubboProvider;
import com.sdg.cmdb.domain.kubernetes.KubernetesServiceCluster;
import com.sdg.cmdb.domain.kubernetes.*;
import com.sdg.cmdb.domain.nginx.NginxTcpDubboDO;
import com.sdg.cmdb.domain.server.EnvTypeEnum;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.util.BeanCopierUtils;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.*;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.dsl.AppsAPIGroupDSL;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class KubernetesServiceImpl implements KubernetesService, InitializingBean {

    //private KubernetesClient client;
    private static final int CONNECTION_TIMEOUT = 3 * 1000;
    private static final int REQUEST_TIMEOUT = 3 * 1000;
    private HashMap<String, KubernetesClient> clusterMap;

    @Autowired
    private KubernetesDao kubernetesDao;

    @Autowired
    private ServerGroupService serverGroupService;

    @Autowired
    private ServerService serverService;

    @Autowired
    private ZookeeperService zookeeperService;

    @Autowired
    private NginxDao nginxDao;

    @Autowired
    private ConfigService configService;

    @Autowired
    private ServerDao serverDao;

    @Autowired
    private CacheKeyService cacheKeyService;

    public static final String CACHE_KEY = "KubernetesServiceImpl:";

    public static final String POD_PHASE_RUNNING = "Running";

    public static final int DEFAULT_SIZE = 4;

    public static final String DUBBO_CLUSTER = "k8s-test";
    public static final String DUBBO_CLUSTER_NAMESPACE = "test";

    private KubernetesClient getClient(String clusterName) {
        if (clusterMap.containsKey(clusterName))
            return clusterMap.get(clusterName);
        return null;
    }

    @Override
    public List<KubernetesClusterVO> queryClusterPage() {

        List<KubernetesClusterDO> clusterList = kubernetesDao.queryCluster();
        List<KubernetesClusterVO> voList = new ArrayList<>();
        for (KubernetesClusterDO cluster : clusterList) {
            List<KubernetesNamespaceDO> namespaceList = kubernetesDao.queryNamespaceByClusterId(cluster.getId());
            KubernetesClusterVO clusterVO = BeanCopierUtils.copyProperties(cluster, KubernetesClusterVO.class);
            clusterVO.setNamespaceList(namespaceList);
            voList.add(clusterVO);
        }
        return voList;
    }

    @Override
    public BusinessWrapper<Boolean> saveCluster(KubernetesClusterVO kubernetesClusterVO) {
        try {
            if (kubernetesClusterVO.getId() == 0) {
                kubernetesDao.addCluster(kubernetesClusterVO);
            } else {
                kubernetesDao.updateCluster(kubernetesClusterVO);
            }
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }

    }

    @Override
    public List<KubernetesNamespaceVO> queryNamespacePage() {
        List<KubernetesNamespaceDO> list = kubernetesDao.queryNamespace();
        List<KubernetesNamespaceVO> voList = new ArrayList<>();
        for (KubernetesNamespaceDO namespaceDO : list)
            voList.add(getNamespaceVO(namespaceDO));
        return voList;
    }

    private KubernetesNamespaceVO getNamespaceVO(long namespaceId) {
        return getNamespaceVO(kubernetesDao.getNamespaceById(namespaceId));
    }

    private KubernetesNamespaceVO getNamespaceVO(KubernetesNamespaceDO namespaceDO) {
        if (namespaceDO == null) return new KubernetesNamespaceVO();
        KubernetesNamespaceVO namespaceVO = BeanCopierUtils.copyProperties(namespaceDO, KubernetesNamespaceVO.class);
        KubernetesClusterDO kubernetesClusterDO = kubernetesDao.getCluster(namespaceDO.getClusterId());
        namespaceVO.setClusterName(kubernetesClusterDO.getName());
        return namespaceVO;
    }

    @Override
    public TableVO<List<KubernetesServiceVO>> queryServicePage(long namespaceId, String name, String portName, int page, int length) {
        int size = kubernetesDao.getServiceSize(namespaceId, name, portName);
        List<KubernetesServiceDO> list = kubernetesDao.queryServicePage(namespaceId, name, portName, page * length, length);
        List<KubernetesServiceVO> voList = new ArrayList<>();
        for (KubernetesServiceDO serviceDO : list) {
            List<KubernetesServicePortDO> servicePortList = kubernetesDao.queryServicePortByServiceId(serviceDO.getId());
            KubernetesServiceVO serviceVO = BeanCopierUtils.copyProperties(serviceDO, KubernetesServiceVO.class);
            serviceVO.setServicePortList(servicePortList);
            serviceVO.setNamespace(getNamespaceVO(serviceDO.getNamespaceId()));
            voList.add(serviceVO);
        }
        return new TableVO<>(size, voList);
    }

    @Override
    public BusinessWrapper<Boolean> saveService(KubernetesServiceVO kubernetesServiceVO) {
        try {
            kubernetesDao.updateService(kubernetesServiceVO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delService(long id) {
        try {
            kubernetesDao.delService(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    public NamespaceList getNamespaceList(String clusterName) {
        NamespaceList namespaceList = new NamespaceList();
        try {
            namespaceList = getClient(clusterName).namespaces().list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return namespaceList;
    }

    public NodeList listNode(String clusterName) {
        NodeList nodeList = new NodeList();
        try {
            nodeList = getClient(clusterName).nodes().list();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return nodeList;
    }

    public ReplicationControllerList listRC(String clusterName) {
        ReplicationControllerList rcList = new ReplicationControllerList();
        try {
            rcList = getClient(clusterName).replicationControllers().list();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rcList;
    }


    public PodList listPods(String clusterName) {
        PodList podList = new PodList();
        try {
            podList = getClient(clusterName).pods().list();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return podList;
    }


    public ServiceList getServiceList(String clusterName) {
        ServiceList serviceList = new ServiceList();
        try {
            serviceList = getClient(clusterName).services().list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceList;
    }

    public AppsAPIGroupDSL getApps(String clusterName) {
        AppsAPIGroupDSL apps = new AppsAPIGroupClient();
        try {
            apps = getClient(clusterName).apps();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apps;
    }

    public MixedOperation<Endpoints, EndpointsList, DoneableEndpoints, Resource<Endpoints, DoneableEndpoints>> getEndpoints(String clusterName) {
        //  AppsAPIGroupDSL endpoints = new AppsAPIGroupClient() ;
        try {
            MixedOperation<Endpoints, EndpointsList, DoneableEndpoints, Resource<Endpoints, DoneableEndpoints>> endpoints = getClient(clusterName).endpoints();
            return endpoints;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public BusinessWrapper<Boolean> sync() {
        List<KubernetesClusterDO> clusterList = kubernetesDao.queryCluster();
        for (KubernetesClusterDO cluster : clusterList)
            syncCluster(cluster.getName());
        return new BusinessWrapper<Boolean>(true);
    }

    /**
     * 同步集群中所有服务
     * @param clusterName
     */
    public void syncCluster(String clusterName) {
        KubernetesClusterDO kubernetesClusterDO = kubernetesDao.getClusterByName(clusterName);
        NamespaceList namespaceList = getNamespaceList(clusterName);
        for (Namespace namespace : namespaceList.getItems()) {
            if (!checkNamespace(namespace.getMetadata().getName(), kubernetesClusterDO.getNamespace())) continue;
            KubernetesNamespaceDO kubernetesNamespaceDO = new KubernetesNamespaceDO(kubernetesClusterDO.getId(), namespace.getMetadata().getName());
            kubernetesNamespaceDO = saveNamespace(kubernetesNamespaceDO);
            syncService(clusterName, kubernetesNamespaceDO);
        }
    }

    /**
     * 同步服务
     * @param clusterName
     * @param kubernetesNamespaceDO
     */
    public void syncService(String clusterName, KubernetesNamespaceDO kubernetesNamespaceDO) {
        ServiceList serviceList = getServiceList(clusterName);
        for (io.fabric8.kubernetes.api.model.Service service : serviceList.getItems()) {
            // 过滤 namespace
            if (!service.getMetadata().getNamespace().equals(kubernetesNamespaceDO.getNamespace())) continue;
            KubernetesServiceDO kubernetesServiceDO = new KubernetesServiceDO(kubernetesNamespaceDO.getId(), service);
            KubernetesServiceDO check = kubernetesDao.getService(kubernetesServiceDO);
            if (check == null) {
                invokeServerGroup(kubernetesServiceDO); // 绑定服务器组
                kubernetesServiceDO = saveService(kubernetesServiceDO);
            }
            saveServicePort(kubernetesServiceDO, service);
        }
    }

    private void invokeServerGroup(KubernetesServiceDO kubernetesServiceDO) {
        if (kubernetesServiceDO.getServerGroupId() != 0) {
            if (serverGroupService.queryServerGroupById(kubernetesServiceDO.getServerGroupId()) != null)
                return;
        }
        String groupName = "group_" + kubernetesServiceDO.getName().replace("-lb", "");
        ServerGroupDO serverGroupDO = serverGroupService.queryServerGroupByName(groupName);
        if (serverGroupDO != null) {
            kubernetesServiceDO.setServerGroupId(serverGroupDO.getId());
            kubernetesServiceDO.setServerGroupName(serverGroupDO.getName());
        }
    }

    private void saveServicePort(KubernetesServiceDO kubernetesServiceDO, io.fabric8.kubernetes.api.model.Service service) {
        delServicePort(kubernetesServiceDO);
        for (ServicePort port : service.getSpec().getPorts()) {
            KubernetesServicePortDO kubernetesServicePortDO = new KubernetesServicePortDO(kubernetesServiceDO.getId(), port);
            kubernetesDao.addPort(kubernetesServicePortDO);
        }
    }

    /**
     * 删除所有ServicePort
     *
     * @param kubernetesServiceDO
     */
    private void delServicePort(KubernetesServiceDO kubernetesServiceDO) {
        List<KubernetesServicePortDO> list = kubernetesDao.queryServicePortByServiceId(kubernetesServiceDO.getId());
        for (KubernetesServicePortDO kubernetesServicePortDO : list)
            kubernetesDao.delPort(kubernetesServicePortDO.getId());
    }

    private KubernetesServiceDO saveService(KubernetesServiceDO kubernetesServiceDO) {
        KubernetesServiceDO check = kubernetesDao.getService(kubernetesServiceDO);
        if (check != null) {
            kubernetesServiceDO.setId(check.getId());
            kubernetesDao.updateService(kubernetesServiceDO);
        } else {
            kubernetesDao.addService(kubernetesServiceDO);
        }
        return kubernetesServiceDO;
    }

    private KubernetesNamespaceDO saveNamespace(KubernetesNamespaceDO kubernetesNamespaceDO) {
        KubernetesNamespaceDO check = kubernetesDao.getNamespace(kubernetesNamespaceDO);
        if (check != null) {
            return check;
        } else {
            kubernetesDao.addNamespace(kubernetesNamespaceDO);
            return kubernetesNamespaceDO;
        }
    }


    private boolean checkNamespace(String namespace, String namespaceConfig) {
        String ns[] = namespaceConfig.split(",");
        for (String n : ns)
            if (namespace.equals(n)) return true;
        return false;
    }


    private KubernetesClient getClient(KubernetesClusterDO kubernetesClusterDO) {
        System.setProperty(Config.KUBERNETES_KUBECONFIG_FILE, kubernetesClusterDO.getKubeconfigFile());
        Config config = new ConfigBuilder().withMasterUrl(kubernetesClusterDO.getMasterUrl()).withTrustCerts(true).build();
        config.setConnectionTimeout(CONNECTION_TIMEOUT);
        config.setRequestTimeout(REQUEST_TIMEOUT);
        return new DefaultKubernetesClient(config);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<KubernetesClusterDO> clusterList = kubernetesDao.queryCluster();
        clusterMap = new HashMap<>();
        for (KubernetesClusterDO cluster : clusterList) {
            try {
                KubernetesClient client = getClient(cluster);
                clusterMap.put(cluster.getName(), client);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public KubernetesServiceDO getKubernetesService(long serverGroupId, int env, String portName) {
        KubernetesClusterDO kubernetesClusterDO = kubernetesDao.queryClusterByEnv(env);
        if (kubernetesClusterDO == null || kubernetesClusterDO.getServerGroupId() == 0)
            return null;
        KubernetesNamespaceDO kubernetesNamespaceDO = kubernetesDao.getNamespace(new KubernetesNamespaceDO(kubernetesClusterDO.getId(), EnvTypeEnum.getDescByCode(env)));
        if (kubernetesNamespaceDO == null) return null;
        return kubernetesDao.queryServiceByPort(kubernetesNamespaceDO.getId(), serverGroupId, portName);
    }

    @Override
    public KubernetesServiceCluster getServerList(long serverGroupId, int env, String portName, int size) {
        if (size <= 0)
            size = DEFAULT_SIZE;
        KubernetesClusterDO kubernetesClusterDO = kubernetesDao.queryClusterByEnv(env);
        KubernetesServiceDO kubernetesServiceDO = getKubernetesService(serverGroupId, env, portName);
        if (kubernetesServiceDO == null) return new KubernetesServiceCluster();
        KubernetesServiceVO kubernetesServiceVO = BeanCopierUtils.copyProperties(kubernetesServiceDO, KubernetesServiceVO.class);
        // nodePort不能为0
        if (kubernetesServiceVO == null || kubernetesServiceVO.getNodePort() == 0)
            return new KubernetesServiceCluster();
        List<ServerDO> nodeList = getServerListByCache(kubernetesClusterDO.getServerGroupId(), size);
        return new KubernetesServiceCluster(nodeList, kubernetesServiceVO);
    }

    private List<ServerDO> getServerListByCache(long serverGroupId, int size) {
        String key = CACHE_KEY + "serverGroupId:" + serverGroupId;
        String cache = cacheKeyService.getKeyByString(key);
        List<ServerDO> result = new ArrayList<>();
        List<ServerDO> cacheServerList = new ArrayList<>();
        List<ServerDO> serverList = new ArrayList<>();
        if (!StringUtils.isEmpty(cache)) {
            Gson gson = new GsonBuilder().create();
            serverList = gson.fromJson(cache, new TypeToken<ArrayList<ServerDO>>() {
            }.getType());
            if (serverList.size() < size)
                serverList = serverService.getServersByGroupId(serverGroupId);
        } else {
            serverList = serverService.getServersByGroupId(serverGroupId);
        }

        for (int i = 0; i < size; i++)
            result.add(serverList.get(i));
        for (int i = size; i < serverList.size(); i++)
            cacheServerList.add(serverList.get(i));
        cacheKeyService.set(key, JSON.toJSONString(cacheServerList), 60 * 24 * 7);
        return result;
    }

    @Override
    public KubernetesServiceVO getService(String dubbo, String cluster, String namespace) {
        HashMap<String, List<String>> map = getPodMap(cluster, namespace);
        return getService(dubbo, map);
    }

    private KubernetesServiceVO getService(String dubbo, HashMap<String, List<String>> podMap) {
        List<DubboProvider> providers = zookeeperService.queryProviders(dubbo);
        for (DubboProvider provider : providers) {
            if (podMap.containsKey(provider.getIp())) {
                List<String> containerListName = podMap.get(provider.getIp());
                for (String containerName : containerListName) {
                    KubernetesServiceDO kubernetesServiceDO = kubernetesDao.getServiceByAppName(containerName);
                    if (kubernetesServiceDO == null) continue;
                    KubernetesServiceVO kubernetesServiceVO = kubernetesDao.queryServiceByPort(kubernetesServiceDO.getNamespaceId(), kubernetesServiceDO.getServerGroupId(), "dubbo");
                    if (kubernetesServiceVO.getNodePort() == 0) continue;
                    kubernetesServiceVO.setPodIp(provider.getIp());
                    return kubernetesServiceVO;
                }
            }
        }
        return null;
    }

    @Override
    public void syncDubbo() {
        String clusterKey = DUBBO_CLUSTER + ":" + DUBBO_CLUSTER_NAMESPACE;
        List<String> dubboList = zookeeperService.getDubboInfo(ZookeeperServiceImpl.DUBBO);
        HashMap<String, List<String>> map = getPodMap(DUBBO_CLUSTER, DUBBO_CLUSTER_NAMESPACE);
        for (String dubbo : dubboList) {
            try {
                NginxTcpDubboDO nginxTcpDubboDO = nginxDao.getNginxTcpDubboByKey(clusterKey, dubbo);
                if (nginxTcpDubboDO != null) continue; // 已录入则跳过
                KubernetesServiceVO kubernetesServiceVO = getService(dubbo, map);
                if (kubernetesServiceVO == null) continue; // 服务不存在
                nginxTcpDubboDO = new NginxTcpDubboDO(clusterKey, kubernetesServiceVO, dubbo);
                nginxDao.addNginxTcpDubbo(nginxTcpDubboDO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param cluster
     * @param namespace
     * @return Map< podIp,容器名:containerName></>
     */
    private HashMap<String, List<String>> getPodMap(String cluster, String namespace) {
        PodList list = listPods(cluster);
        HashMap<String, List<String>> map = new HashMap<>();
        for (Pod p : list.getItems()) {
            if (!p.getMetadata().getNamespace().equals(namespace)) continue;
            List<Container> containers = p.getSpec().getContainers();
            // 遍历 Pod中的容器
            List<String> containerNameList = new ArrayList<>();
            for (Container c : containers) {
                containerNameList.add(c.getName());
            }
            map.put(p.getStatus().getPodIP(), containerNameList);
        }
        return map;
    }

    @Override
    public BusinessWrapper<Boolean> syncClusterLabel(String clusterName) {
        KubernetesClusterDO kubernetesClusterDO = kubernetesDao.getClusterByName(clusterName);
        for (String namespace : kubernetesClusterDO.getNamespace().split(","))
            syncClusterPod(clusterName, namespace);
        return new BusinessWrapper<Boolean>(true);
    }


    private boolean syncClusterPod(String clusterName, String namespace) {
        PodList list = listPods(clusterName);
        HashMap<String, String> map = new HashMap<>();

        for (Pod pod : list.getItems()) {
            try {
                // 判断命名空间
                if (!pod.getMetadata().getNamespace().equals(namespace)) continue;
                // 判断Pod是否在运行
                if (!pod.getStatus().getPhase().equals(POD_PHASE_RUNNING)) continue;
                String content = "";
                if (map.containsKey(pod.getStatus().getHostIP()))
                    content = map.get(pod.getStatus().getHostIP());
                for (Container container : pod.getSpec().getContainers())
                    content += container.getName() + " ";
                map.put(pod.getStatus().getHostIP(), content);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        invokeKubernetsLabel(map);
        return true;
    }

    /**
     * 往服务器组属性写入标签
     *
     * @param map
     */
    private void invokeKubernetsLabel(HashMap<String, String> map) {
        for (String ip : map.keySet()) {
            ServerDO serverDO = serverDao.queryServerByInsideIp(ip);
            if (serverDO == null) continue;
            configService.saveConfigServerValue(serverDO, ConfigServerGroupServiceImpl.KUBERNETES_LABEL, map.get(ip));
        }
    }

}
