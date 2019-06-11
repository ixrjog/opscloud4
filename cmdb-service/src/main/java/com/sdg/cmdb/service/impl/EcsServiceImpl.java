package com.sdg.cmdb.service.impl;

import com.alibaba.fastjson.JSONArray;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.aliyun.AliyunNetworkDO;
import com.sdg.cmdb.domain.aliyun.AliyunPrepaidInstance;
import com.sdg.cmdb.domain.aliyun.AliyunRenewInstance;
import com.sdg.cmdb.domain.aliyun.AliyunRenewInstances;
import com.sdg.cmdb.domain.ip.IPDetailDO;
import com.sdg.cmdb.domain.ip.IPDetailVO;
import com.sdg.cmdb.domain.server.*;
import com.sdg.cmdb.service.*;

import com.sdg.cmdb.util.TimeUtils;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liangjian on 2017/1/13.
 */
@Service
public class EcsServiceImpl implements EcsService {

    private static final Logger logger = LoggerFactory.getLogger(EcsServiceImpl.class);
    private static final Logger coreLogger = LoggerFactory.getLogger("coreLogger");

    @Resource
    private SchedulerManager schedulerManager;

    @Value(value = "${aliyun.ecs.public.network.id}")
    private String publicNetworkId;

    /**
     * 美国西部1
     */
    public static final String regionIdUsWest1 = "us-west-1";
    /**
     * 华东1
     */
    public static final String regionIdCnHangzhou = "cn-hangzhou";
    /**
     * 香港
     */
    public static final String regionIdCnHongkong = "cn-hongkong";

    // 查询的区域:华东1(cn-hangzhou),华北1（cn-qingdao),华东2金融(cn-shanghai-finance-1),华南1金融(cn-shenzhen-finance-1)
    /**
     * 金融 华东1
     */
    public static final String regionIdFinanceCnHangzhou = "cn-hangzhou";


    @Resource
    private ServerDao serverDao;

    @Autowired
    private ServerService serverService;

    @Autowired
    private IPService ipService;

    @Autowired
    private AliyunService aliyunService;

    @Autowired
    private AliyunInstanceService aliyunInstanceService;


    /**
     * 获取ECS服务器分页数据
     *
     * @param serverName
     * @param status
     * @param page
     * @param length
     * @return
     */
    @Override
    public TableVO<List<EcsServerVO>> getEcsServerPage(String serverName, String queryIp, int status, String chargeType, int page, int length) {
        long size = serverDao.getEcsServerSize(serverName, queryIp, status, chargeType);
        List<EcsServerDO> list = serverDao.getEcsServerPage(serverName, queryIp, status, chargeType, page * length, length);
        List<EcsServerVO> listVO = new ArrayList<>();
        for (EcsServerDO ecsServerDO : list) {
            EcsServerVO ecsServerVO = new EcsServerVO(ecsServerDO);
            invokeEcsServerVO(ecsServerVO);
            listVO.add(ecsServerVO);
        }
        return new TableVO<>(size, listVO);
    }

    @Override
    public TableVO<List<EcsServerVO>> getEcsRenewPage(String serverName, String queryIp, int status, int day, int page, int length) {
        // TODO 计算查询参数 过期日期
        if (day <= 0) day = 30;
        String expiredTime = TimeUtils.futureTime(day);
        long size = serverDao.getEcsRenewSize(serverName, queryIp, status, expiredTime);
        List<EcsServerDO> list = serverDao.getEcsRenewPage(serverName, queryIp, status, expiredTime, page * length, length);
        List<EcsServerVO> listVO = new ArrayList<>();
        for (EcsServerDO ecsServerDO : list) {
            EcsServerVO ecsServerVO = new EcsServerVO(ecsServerDO);
            invokeEcsServerVO(ecsServerVO);
            listVO.add(ecsServerVO);
        }
        return new TableVO<>(size, listVO);
    }

    /**
     * 获取ECS模版分页数据
     *
     * @param zoneId
     * @param page
     * @param length
     * @return
     */
    public TableVO<List<EcsTemplateDO>> getEcsTemplatePage(String zoneId, int page, int length) {
        long size = serverDao.getEcsTemplateSize(zoneId);
        List<EcsTemplateDO> list = serverDao.getEcsTemplatePage(zoneId, page * length, length);
        return new TableVO<>(size, list);
    }



    public List<EcsServerDO> ecsGetAll() {
        List<EcsServerDO> servers = new ArrayList<EcsServerDO>();
        for (String regionId : aliyunService.acqRegionIds()) {
            servers.addAll(ecsGetAll(regionId));
        }
        return servers;
    }

    /**
     * 查询当前区域所有服务器属性
     *
     * @param regionId
     * @return
     */
    private List<EcsServerDO> ecsGetAll(String regionId) {
        List<DescribeInstancesResponse.Instance> instanceList = aliyunInstanceService.getInstanceList(regionId);
        List<EcsServerDO> ecsList = new ArrayList<EcsServerDO>();
        for (DescribeInstancesResponse.Instance instance : instanceList) {
            EcsServerDO ecs = new EcsServerDO(instance);
            ecsList.add(ecs);
        }
        return ecsList;
    }

    /**
     * 此接口废弃
     *
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> ecsRefresh() {
        List<EcsServerDO> servers = ecsGetAll();
        for (EcsServerDO server : servers) {
            updateEcsServerForServer(server);
        }
        return new BusinessWrapper<>(true);
    }

    /**
     * 同步并更新ECS列表
     *
     * @param type 0 阿里云 1 金融云
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> ecsSync(int type) {
        if (type == 0)
            return new BusinessWrapper<>(ecsSyncByAliyun());
        return new BusinessWrapper<>(true);
    }

    /**
     * 同步阿里云ECS
     *
     * @return
     */
    public boolean ecsSyncByAliyun() {
        // 获取阿里云ECS
        List<EcsServerDO> servers = ecsGetAll();
        // TODO KEY=insideIp   server表中所有的Ecs
        HashMap<String, ServerDO> serverMap = acqServerMapEcs();
        // TODO KEY=instanceId
        HashMap<String, EcsServerDO> aliyunEcsMap = new HashMap<>();
        try {
            // TODO 遍历ECS，并校验server表数据是否存在
            for (EcsServerDO ecsServerDO : servers) {
                aliyunEcsMap.put(ecsServerDO.getInstanceId(), ecsServerDO);
                // TODO server表已经存在,标记已录入
                if (serverMap.containsKey(ecsServerDO.getInsideIp())) {
                    ServerDO serverDO = serverMap.get(ecsServerDO.getInsideIp());
                    ecsServerDO.setContent(serverDO.getContent());
                    ecsServerDO.setServerId(serverDO.getId());
                    ecsServerDO.setStatus(EcsServerDO.statusAssociate);
                    updateServerPublicIp(serverDO, ecsServerDO);
                    // TODO 在serverMap(server表中所有的Ecs)中移除,剩下的就是下线服务器
                    serverMap.remove(ecsServerDO.getInsideIp());
                } else {
                    // TODO server表不存在,新ECS服务器
                    ecsServerDO.setStatus(EcsServerDO.statusNew);
                }
                invokeDisk(ecsServerDO);
                saveEcsServer(ecsServerDO);
            }
            // TODO 处理server表中存在的但ECS中不存在的数据（ECS服务器下线）
            for (Map.Entry<String, ServerDO> entry : serverMap.entrySet()) {
                ServerDO serverDO = entry.getValue();
                EcsServerDO ecsServerDO = serverDao.queryEcsByInsideIp(serverDO.getInsideIp());
                if (ecsServerDO == null) {
                    ecsServerDO = new EcsServerDO(serverDO, EcsServerDO.statusOffline);
                    saveEcsServer(ecsServerDO);
                }
            }
            checkEcs(aliyunEcsMap);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 校验ECS表中多余的服务器（ECS服务器下线）
     *
     * @param aliyunEcsMap API获取的所有ECS
     */
    private void checkEcs(HashMap<String, EcsServerDO> aliyunEcsMap) {
        HashMap<String, EcsServerDO> ecsServerMap = acqEcsServerMap();
        for (Map.Entry<String, EcsServerDO> entry : aliyunEcsMap.entrySet()) {
            // 阿里云 API ECS
            EcsServerDO ecsServerDO = entry.getValue();
            // ECS如果存在则在map中移除
            if (ecsServerMap.containsKey(ecsServerDO.getInstanceId()))
                ecsServerMap.remove(ecsServerDO.getInstanceId());
        }
        // 标记下线服务器
        for (Map.Entry<String, EcsServerDO> entry : ecsServerMap.entrySet()) {
            EcsServerDO ecsServerDO = entry.getValue();
            ecsServerDO.setStatus(EcsServerDO.statusOffline);
            saveEcsServer(ecsServerDO);
        }
    }


    /**
     * 从DB获取所有ECS的Map
     *
     * @return
     */
    private HashMap<String, EcsServerDO> acqEcsServerMap() {
        HashMap<String, EcsServerDO> map = new HashMap<>();
        List<EcsServerDO> servers = serverDao.getEcsServerAll();
        for (EcsServerDO ecsServerDO : servers)
            map.put(ecsServerDO.getInstanceId(), ecsServerDO);
        return map;

    }

    /**
     * 从AliyunAPI获取所有ECS的Map
     *
     * @return
     */
    private HashMap<String, ServerDO> acqServerMapEcs() {
        List<ServerDO> servers = serverDao.getServerByType(ServerDO.ServerTypeEnum.ecs.getCode());
        HashMap<String, ServerDO> map = new HashMap<>();
        for (ServerDO server : servers)
            map.put(server.getInsideIp(), server);
        return map;
    }


    @Override
    public BusinessWrapper<Boolean> ecsCheck() {
        //查询server中没有登记的ecsServer
        List<EcsServerDO> servers = acqEcsExclude();
        for (EcsServerDO ecs : servers) {
            saveEcsServer(ecs);
        }
        //查询server中对比ecsServer多余的服务器
        servers = acqServerExclude();
        for (EcsServerDO ecs : servers) {
            saveEcsServer(ecs);
        }
        return new BusinessWrapper<>(true);
    }




    /**
     * 查询ecs
     *
     * @param regionId
     * @return
     */
    private EcsServerDO ecsGet(String regionId, ServerDO serverDO) {
        if (serverDO == null) return null;
        DescribeInstancesResponse.Instance instance = aliyunInstanceService.getInstanceByIp(regionId,serverDO.getInsideIp());
        return new EcsServerDO(instance);
    }

    @Override
    public EcsServerDO ecsGet(String regionId, String instanceId) {
        DescribeInstancesResponse.Instance instance = aliyunInstanceService.getInstance(regionId, instanceId);
        try {
            return new EcsServerDO(instance);
        } catch (Exception e) {
            return new EcsServerDO();
        }
    }




    /**
     * 停止实例
     *
     * @param regionId
     * @param forceStop 重启机器时的是否强制关机策略。默认值为 false
     * @return
     */
    @Override
    public boolean ecsStop(String regionId, boolean forceStop, ServerDO serverDO) {
        if (serverDO == null) return false;
        if (regionId == null) regionId = regionIdCnHangzhou;
        EcsServerDO ecs = ecsGet(regionId, serverDO);
        //ecs不存在
        if (ecs == null) return false;
        StopInstanceRequest describe = new StopInstanceRequest();
        //describe.setActionName("ModifyInstanceAttribute");
        describe.setInstanceId(ecs.getInstanceId());
        if (forceStop == true) describe.setForceStop(true);
        StopInstanceResponse response = sampleStopInstanceResponse(regionId, describe);
        if (response == null || response.getRequestId().isEmpty()) return false;
        return true;
    }

    /**
     * 启动实例
     *
     * @param instanceId
     * @return
     */
    @Override
    public boolean powerOn(String instanceId) {
        StartInstanceRequest describe = new StartInstanceRequest();
        describe.setInstanceId(instanceId);

        schedulerManager.registerJob(() -> {
            try {
                Thread.sleep(20000);
                StartInstanceResponse response = sampleStartInstanceResponse(regionIdCnHangzhou, describe);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        return true;
    }

    /**
     * 重启实例(服务器正常启动状态才能使用)
     *
     * @param regionId
     * @return
     */
    @Override
    public boolean ecsReboot(String regionId, ServerDO serverDO) {
        if (serverDO == null) return false;
        if (regionId == null) regionId = regionIdCnHangzhou;
        EcsServerDO ecs = ecsGet(regionId, serverDO);
        //ecs不存在
        if (ecs == null) return false;
        RebootInstanceRequest describe = new RebootInstanceRequest();
        describe.setInstanceId(ecs.getInstanceId());
        RebootInstanceResponse response = sampleRebootInstanceResponse(regionId, describe);
        if (response == null || response.getRequestId().isEmpty()) return false;
        return true;
    }


    private IAcsClient acqIAcsClient(String regionId) {
        return aliyunService.acqIAcsClient(regionId);
    }


//    private DescribeInstancesResponse sampleDescribeInstancesResponse(String regionId, DescribeInstancesRequest describe) {
//        if (regionId == null) regionId = regionIdCnHangzhou;
//        IAcsClient client;
//
//        client = acqIAcsClient(regionId);
//
//        try {
//            DescribeInstancesResponse response
//                    = client.getAcsResponse(describe);
//            return response;
//        } catch (ServerException e) {
//            e.printStackTrace();
//            return null;
//        } catch (ClientException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }



    /**
     * 启动ecs
     *
     * @param regionId
     * @param describe
     * @return
     */
    private StartInstanceResponse sampleStartInstanceResponse(String regionId, StartInstanceRequest describe) {
        if (regionId == null) regionId = regionIdCnHangzhou;
        IAcsClient client = acqIAcsClient(regionId);
        try {
            StartInstanceResponse response
                    = client.getAcsResponse(describe);
            return response;
        } catch (ServerException e) {
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 停止ecs
     *
     * @param regionId
     * @param describe
     * @return
     */
    private StopInstanceResponse sampleStopInstanceResponse(String regionId, StopInstanceRequest describe) {
        if (regionId == null) regionId = regionIdCnHangzhou;
        IAcsClient client = acqIAcsClient(regionId);
        try {
            StopInstanceResponse response
                    = client.getAcsResponse(describe);
            return response;
        } catch (ServerException e) {
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 重启ecs
     *
     * @param regionId
     * @param describe
     * @return
     */
    private RebootInstanceResponse sampleRebootInstanceResponse(String regionId, RebootInstanceRequest describe) {
        if (regionId == null) regionId = regionIdCnHangzhou;
        IAcsClient client = acqIAcsClient(regionId);
        try {
            RebootInstanceResponse response
                    = client.getAcsResponse(describe);
            return response;
        } catch (ServerException e) {
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }



    /**
     * 保存ecs
     *
     * @param ecsServerDO
     * @return
     */
    private boolean saveEcsServer(EcsServerDO ecsServerDO) {
        EcsServerDO ecs = serverDao.queryEcsByInsideIp(ecsServerDO.getInsideIp());
        if (ecs == null) {
            return addEcsServer(ecsServerDO);
        } else {
            ecsServerDO.setId(ecs.getId());
            return updateEcsServer(ecsServerDO);
        }
    }

    /**
     * 新增ecs
     *
     * @param ecsServerDO
     * @return
     */
    private boolean addEcsServer(EcsServerDO ecsServerDO) {
        try {
            serverDao.addEcsServer(ecsServerDO);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 更新ecs
     *
     * @param ecsServerDO
     * @return
     */
    private boolean updateEcsServer(EcsServerDO ecsServerDO) {
        try {
            serverDao.updateEcsServer(ecsServerDO);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 从server表更新ecsServer数据（content,serverId 字段数据）
     *
     * @param ecsServerDO
     * @return
     */
    @Override
    public boolean updateEcsServerForServer(EcsServerDO ecsServerDO) {
        if (ecsServerDO == null) return false;
        ServerDO serverDO = serverDao.queryServerByInsideIp(ecsServerDO.getInsideIp());
        if (serverDO != null) {
            ecsServerDO.setContent(serverDO.getContent());
            ecsServerDO.setServerId(serverDO.getId());
            ecsServerDO.setStatus(EcsServerDO.statusAssociate);
            invokeDisk(ecsServerDO);
            //更新公网ip
            updateServerPublicIp(serverDO, ecsServerDO);
        } else {
            ecsServerDO.setStatus(EcsServerDO.statusNew);
        }
        return saveEcsServer(ecsServerDO);
    }

    /**
     * 从server表更新ecsServer数据（content,serverId 字段数据）
     *
     * @param serverDO
     * @return
     */
    @Override
    public boolean updateEcsServerForServer(ServerDO serverDO) {
        if (serverDO == null) return false;
        EcsServerDO ecsServerDO = serverDao.queryEcsByInsideIp(serverDO.getInsideIp());
        if (ecsServerDO == null) return false;
        ecsServerDO.setContent(serverDO.getContent());
        ecsServerDO.setServerId(serverDO.getId());
        ecsServerDO.setStatus(EcsServerDO.statusAssociate);
        invokeDisk(ecsServerDO);
        //更新公网ip
        updateServerPublicIp(serverDO, ecsServerDO);
        return saveEcsServer(ecsServerDO);
    }

    public void updateServerPublicIp(ServerDO serverDO, EcsServerDO ecsServerDO) {
        long networkId = Long.valueOf(publicNetworkId);
        // ecs无公网ip
        if (StringUtils.isEmpty(ecsServerDO.getPublicIp())) return;
        // server有公网ip
        if (!StringUtils.isEmpty(serverDO.getPublicIp())) return;

        IPDetailDO publicDO = new IPDetailDO(networkId, ecsServerDO.getPublicIp(), IPDetailDO.PUBLIC_IP);
        IPDetailVO publicVO = ipService.getIPDetail(publicDO);

        if (publicVO == null) {
            ipService.saveGroupIP(publicDO);
        } else {
            publicDO.setId(publicVO.getId());
        }
        //serverDO.setPublicIpId(publicVO == null ? publicDO.getId() : publicVO.getId());
        final IPDetailDO finalPublicDO = publicDO;
        serverDO.setPublicIp(ecsServerDO.getPublicIp());
        serverDO.setPublicIpId(publicDO.getId());
        serverDao.updateServerGroupServer(serverDO);
        // 标记server占用新的ip
        if (finalPublicDO != null) {
            finalPublicDO.setServerId(serverDO.getId());
            ipService.saveGroupIP(finalPublicDO);
        }

    }

    /**
     * 下线  查询server中对比ecsServer多余的服务器
     */
    public List<EcsServerDO> acqServerExclude() {
        List<ServerDO> allServer = serverDao.queryServerByServerType(ServerDO.ServerTypeEnum.ecs.getCode());
        List<EcsServerDO> allEcs = ecsGetAll();
        for (EcsServerDO ecs : allEcs) {
            for (ServerDO server : allServer) {
                if (server.getInsideIp().equals(ecs.getInsideIp())) {
                    allServer.remove(server);
                    break;
                }
            }
        }
        List<EcsServerDO> servers = new ArrayList<EcsServerDO>();
        for (ServerDO serverDO : allServer) {
            servers.add(new EcsServerDO(serverDO, EcsServerDO.statusOffline));
        }
        return servers;
    }

    /**
     * 查询server中没有登记的ecsServer
     *
     * @return
     */
    public List<EcsServerDO> acqEcsExclude() {
        List<ServerDO> allServer = serverDao.queryServerByServerType(ServerDO.ServerTypeEnum.ecs.getCode());
        List<EcsServerDO> servers = ecsGetAll();
        for (ServerDO server : allServer) {
            for (EcsServerDO ecs : servers) {
                // 匹配内网ip;匹配则删除
                if (server.getInsideIp().equals(ecs.getInsideIp())) {
                    servers.remove(ecs);
                    break;
                }
            }
        }
        for (EcsServerDO ecs : servers) {
            ecs.setStatus(EcsServerDO.statusNew);
        }
        return servers;
    }

    /**
     * 设置ECS状态
     *
     * @param insideIp
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> setStatus(String insideIp, int status) {
        EcsServerDO ecs = serverDao.queryEcsByInsideIp(insideIp);
        if (ecs != null) {
            ecs.setStatus(status);
            updateEcsServer(ecs);
        }
        return new BusinessWrapper<>(true);
    }

    /**
     * 删除ECS & Server
     *
     * @param insideIp
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> delEcs(String insideIp) {
        EcsServerDO ecs = serverDao.queryEcsByInsideIp(insideIp);
        ServerDO server = serverDao.queryServerByInsideIp(insideIp);
        if (ecs != null) {
            serverDao.delEcsServerById(ecs.getId());
        }
        if (server != null) {
            serverService.delServerGroupServer(server.getId());
        }
        return new BusinessWrapper<>(true);
    }

    @Override
    public ServerStatisticsDO statistics() {
        ServerStatisticsDO serverStatisticsDO = serverDao.queryEcsStatistics();
        serverStatisticsDO.setServerType(ServerStatisticsDO.ServerTypeEnum.ecsServer.getCode());
        return serverStatisticsDO;
    }

    private void invokeDisk(EcsServerDO ecsServerDO) {
        List<DescribeDisksResponse.Disk> disks = aliyunInstanceService.queryDisks(ecsServerDO.getRegionId(), ecsServerDO.getInstanceId());
        try {
            for (DescribeDisksResponse.Disk disk : disks) {
                if (disk.getType().equals("system")) {
                    ecsServerDO.setSystemDiskCategory("system");
                    ecsServerDO.setSystemDiskSize(disk.getSize());
                    continue;
                }
                if (disk.getType().equals("data")) {
                    ecsServerDO.setDataDiskCategory("data");
                    ecsServerDO.setDataDiskSize(disk.getSize());
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置ecs属性
     *
     * @param ecsServerDO
     * @param propertyType
     * @param value
     * @return
     */
    @Override
    public boolean setEcsProperty(EcsServerDO ecsServerDO, int propertyType, String value) {
        EcsPropertyDO ecsPropertyDO = serverDao.queryEcsProperty(ecsServerDO.getInstanceId(), propertyType, value);
        if (ecsPropertyDO != null) return false;

        ecsPropertyDO = new EcsPropertyDO(ecsServerDO, propertyType, value);
        try {
            serverDao.addEcsProperty(ecsPropertyDO);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public EcsPropertyDO getEcsProperty(EcsServerDO ecsServerDO, int propertyType) {
        List<EcsPropertyDO> list = getEcsPropertyAll(ecsServerDO, propertyType);
        if (list.size() >= 1) return list.get(0);
        return new EcsPropertyDO();
    }

    @Override
    public List<EcsPropertyDO> getEcsPropertyAll(EcsServerDO ecsServerDO, int propertyType) {
        this.checkEcsProperty(ecsServerDO, propertyType);
        List<EcsPropertyDO> list = serverDao.queryEcsPropertyAll(ecsServerDO.getInstanceId(), propertyType);
        return list;
    }

    private void checkEcsProperty(EcsServerDO ecsServerDO, int propertyType) {
        List<EcsPropertyDO> list = serverDao.queryEcsPropertyAll(ecsServerDO.getInstanceId(), propertyType);
        if (list.size() == 0)
            saveEcsServerProperty(ecsServerDO);
    }

    /**
     * 保存ecsServer的所有属性
     *
     * @param ecsServerDO
     * @return
     */
    @Override
    public boolean saveEcsServerProperty(EcsServerDO ecsServerDO) {
        DescribeInstancesResponse.Instance instance = aliyunInstanceService.getInstance(ecsServerDO.getRegionId(), ecsServerDO.getInstanceId());
        if (instance == null || StringUtils.isEmpty(instance.getInstanceId())) return false;
        List<EcsPropertyDO> list = new ArrayList<>();
        // networkType
        EcsPropertyDO propertyNetworkType = new EcsPropertyDO(ecsServerDO, EcsPropertyDO.PropertyTypeEnum.networkType.getCode(), instance.getInstanceNetworkType());
        list.add(propertyNetworkType);
        // vpcId
        if (propertyNetworkType.getPropertyValue().equalsIgnoreCase("vpc")) {
            EcsPropertyDO propertyVpcId = new EcsPropertyDO(ecsServerDO, EcsPropertyDO.PropertyTypeEnum.vpcId.getCode(), instance.getVpcAttributes().getVpcId());
            list.add(propertyVpcId);
            // vswitchId
            EcsPropertyDO propertyVswitchId = new EcsPropertyDO(ecsServerDO, EcsPropertyDO.PropertyTypeEnum.vswitchId.getCode(), instance.getVpcAttributes().getVSwitchId());
            list.add(propertyVswitchId);
        }
        // securityGroupId
        for (String securityGroupId : instance.getSecurityGroupIds()) {
            EcsPropertyDO propertySecurityGroupId = new EcsPropertyDO(ecsServerDO, EcsPropertyDO.PropertyTypeEnum.securityGroupId.getCode(), securityGroupId);
            list.add(propertySecurityGroupId);
        }
        // imageId
        if (!StringUtils.isEmpty(instance.getImageId())) {
            EcsPropertyDO propertyImageId = new EcsPropertyDO(ecsServerDO, EcsPropertyDO.PropertyTypeEnum.imageId.getCode(), instance.getImageId());
            list.add(propertyImageId);
        }
        try {
            for (EcsPropertyDO ecsPropertyDO : list)
                saveEcsProperty(ecsPropertyDO);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean saveEcsProperty(EcsPropertyDO ecsPropertyDO) {
        EcsPropertyDO propertyDO = serverDao.queryEcsProperty(ecsPropertyDO.getInstanceId(), ecsPropertyDO.getPropertyType(), ecsPropertyDO.getPropertyValue());
        if (propertyDO != null) return true;
        try {
            serverDao.addEcsProperty(ecsPropertyDO);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delEcsProperty(ServerDO serverDO) {
        if (serverDO == null) return false;
        List<EcsPropertyDO> list = serverDao.queryEcsPropertyByServerId(serverDO.getId());
        try {
            for (EcsPropertyDO ecsPropertyDO : list)
                serverDao.delEcsProperty(ecsPropertyDO.getId());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void invokeEcsServerVO(EcsServerVO ecsServerVO) {
        try {
            EcsPropertyDO networkTypePropertyDO = getEcsProperty(ecsServerVO, EcsPropertyDO.PropertyTypeEnum.networkType.getCode());
            ecsServerVO.setNetworkTypePropertyDO(networkTypePropertyDO);
            // 如果是vpc网络
            if (networkTypePropertyDO.getPropertyValue().equalsIgnoreCase(AliyunNetworkDO.NetworkTypeEnum.vpc.getDesc())) {
                ecsServerVO.setVpcPropertyDO(getEcsProperty(ecsServerVO, EcsPropertyDO.PropertyTypeEnum.vpcId.getCode()));
                ecsServerVO.setVswitchPropertyDO(getEcsProperty(ecsServerVO, EcsPropertyDO.PropertyTypeEnum.vswitchId.getCode()));
            }
            ecsServerVO.setImagePropertyDO(getEcsProperty(ecsServerVO, EcsPropertyDO.PropertyTypeEnum.imageId.getCode()));
            ecsServerVO.setSecurityGroupPropertyDO(getEcsProperty(ecsServerVO, EcsPropertyDO.PropertyTypeEnum.securityGroupId.getCode()));
            ecsServerVO.setExpiredDay(TimeUtils.expiredDay(ecsServerVO.getExpiredTime()));
        } catch (Exception e) {
        }
    }

    @Override
    public BusinessWrapper<Boolean> renewInstances(AliyunRenewInstances aliyunRenewInstances) {
        boolean result = true;
        for (AliyunRenewInstance renewInstance : aliyunRenewInstances.getEcsInstances()) {
            if (renewInstance.getPeriod() == 0)
                renewInstance.setPeriod(aliyunRenewInstances.getPeriod());
            if (!renewInstance(EcsServiceImpl.regionIdCnHangzhou, renewInstance)) {
                result = false;
            } else {
                // TODO 续费成功更新实例信息
                DescribeInstancesResponse.Instance instance = aliyunInstanceService.getInstance(null,renewInstance.getInstanceId());
                if (instance != null)
                    saveInstance(instance);
            }

        }
        return new BusinessWrapper<Boolean>(result);
    }

    /**
     * 保存ECS实例
     */
    public boolean saveInstance(DescribeInstancesResponse.Instance instance) {
        try {
            EcsServerDO server = serverDao.queryEcsByInstanceId(instance.getInstanceId());
            EcsServerDO ecsInstance = new EcsServerDO(instance);
            ecsInstance.setServerId(server.getServerId());
            ecsInstance.setContent(server.getContent());
            ecsInstance.setServerName(server.getServerName());
            ecsInstance.setStatus(server.getStatus());
            ecsInstance.setId(server.getId());
            updateEcsServer(ecsInstance);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public BusinessWrapper<Boolean> prepaidInstance(AliyunPrepaidInstance prepaidInstance) {
        ModifyInstanceChargeTypeRequest request = new ModifyInstanceChargeTypeRequest();
        JSONArray instanceIds = new JSONArray();
        instanceIds.add(prepaidInstance.getInstanceId());
        request.setInstanceIds(instanceIds.toJSONString());
        request.setPeriod(prepaidInstance.getPeriod());
        request.setInstanceChargeType("PrePaid");
        request.setPeriodUnit("Month");
        request.setIncludeDataDisks(true);
        IAcsClient client;
        client = acqIAcsClient(EcsServiceImpl.regionIdCnHangzhou);
        try {
            ModifyInstanceChargeTypeResponse response
                    = client.getAcsResponse(request);
            // TODO 有订单编号，转预付费成功
            if (!StringUtils.isEmpty(response.getOrderId())) {
                savePrepaidInstance(prepaidInstance.getInstanceId(), 5);
                return new BusinessWrapper<Boolean>(true);
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BusinessWrapper<Boolean>(false);
    }

    private boolean savePrepaidInstance(String instanceId, int retry) {
        for (int i = 1; i <= retry; i++) {
            try {
                Thread.sleep(1000);
                DescribeInstancesResponse.Instance instance = aliyunInstanceService.getInstance(null,instanceId);
                if (instance.getInstanceChargeType().equals("PrePaid"))
                    return saveInstance(instance);
            } catch (Exception e) {

            }
        }
        return false;
    }

    /**
     * 续费
     *
     * @param regionId
     * @param aliyunRenewInstance
     * @return
     */
    private boolean renewInstance(String regionId, AliyunRenewInstance aliyunRenewInstance) {
        IAcsClient client;
        RenewInstanceRequest request = new RenewInstanceRequest();
        request.setInstanceId(aliyunRenewInstance.getInstanceId());
        request.setPeriod(aliyunRenewInstance.getPeriod());
        // request.setPeriodUnit("Month"); 默认值
        client = acqIAcsClient(regionId);
        try {
            RenewInstanceResponse response
                    = client.getAcsResponse(request);
            if (!StringUtils.isEmpty(response.getRequestId()))
                return true;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }


}
