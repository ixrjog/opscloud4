package com.sdg.cmdb.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.aliyun.AliyunNetworkDO;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.AliyunEcsItemEnum;
import com.sdg.cmdb.domain.ip.IPDetailDO;
import com.sdg.cmdb.domain.ip.IPDetailVO;
import com.sdg.cmdb.domain.server.*;
import com.sdg.cmdb.service.AliyunService;
import com.sdg.cmdb.service.ConfigCenterService;
import com.sdg.cmdb.service.EcsService;
import com.sdg.cmdb.service.IPService;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private ConfigCenterService configCenterService;

    @Resource
    private SchedulerManager schedulerManager;


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

    /**
     * 阿里云
     */
    public static final int aliyunEcs = 0;

    /**
     * 金融云
     */
    public static final int aliyunFinanceEcs = 1;

    private String[] acqRegionIds() {
        HashMap<String, String> configMap = acqConifMap();
        String aliyunRegionId = configMap.get(AliyunEcsItemEnum.ALIYUN_ECS_REGION_ID.getItemKey());
        String[] regionIds = aliyunRegionId.split(",");
        return regionIds;
    }

    private String[] acqFinanceRegionIds() {
        HashMap<String, String> configMap = acqConifMap();
        String aliyunRegionId = configMap.get(AliyunEcsItemEnum.ALIYUN_FINANCE_ECS_REGION_ID.getItemKey());
        //System.err.println(aliyunRegionId);
        String[] regionIds = aliyunRegionId.split(",");
        return regionIds;
    }


    @Resource
    private ServerDao serverDao;

    @Resource
    private IPService ipService;

    @Resource
    private AliyunService aliyunService;

    private HashMap<String, String> configMap;


    private HashMap<String, String> acqConifMap() {
        if (configMap != null) return configMap;
        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.ALIYUN_ECS.getItemKey());
    }

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
    public TableVO<List<EcsServerVO>> getEcsServerPage(String serverName, String queryIp, int status, int page, int length) {
        long size = serverDao.getEcsServerSize(serverName, queryIp, status);
        List<EcsServerDO> list = serverDao.getEcsServerPage(serverName, queryIp, status, page * length, length);

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


    /**
     * 查询当前区域服务器总数
     *
     * @param RegionId
     * @return
     */
    private int ecsTotalCount(String RegionId, boolean isFinance) {
        DescribeInstancesRequest describe = new DescribeInstancesRequest();
        DescribeInstancesResponse response = sampleDescribeInstancesResponse(RegionId, describe, isFinance);
        return response.getTotalCount();
    }


    private List<EcsServerDO> ecsGetAll() {
        List<EcsServerDO> servers = new ArrayList<EcsServerDO>();
        for (String regionId : acqRegionIds()) {
            servers.addAll(ecsGetAll(regionId));
        }
        return servers;
    }

    private List<EcsServerDO> ecsFinanceGetAll() {
        List<EcsServerDO> servers = new ArrayList<EcsServerDO>();
        for (String regionId : acqFinanceRegionIds()) {
            servers.addAll(ecsFinanceGetAll(regionId));
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
        if (regionId == null) regionId = regionIdCnHangzhou;
        List<EcsServerDO> allEcs = new ArrayList<EcsServerDO>();
        int pageSize = 50;
        DescribeInstancesRequest describe = new DescribeInstancesRequest();
        describe.setPageSize(pageSize);
        DescribeInstancesResponse response = sampleDescribeInstancesResponse(regionId, describe, false);
        invoke(allEcs, response);
        //获取总数
        int totalCount = response.getTotalCount();
        // 循环次数
        int cnt = (totalCount + pageSize - 1) / pageSize;
        for (int i = 1; i < cnt; i++) {
            describe.setPageNumber(i + 1);
            response = sampleDescribeInstancesResponse(regionId, describe, false);
            invoke(allEcs, response);
        }
        return allEcs;
    }

    /**
     * 查询金融云区域所有服务器属性
     *
     * @param regionId
     * @return
     */
    private List<EcsServerDO> ecsFinanceGetAll(String regionId) {
        if (regionId == null) regionId = regionIdCnHangzhou;
        List<EcsServerDO> allEcs = new ArrayList<EcsServerDO>();
        int pageSize = 50;
        DescribeInstancesRequest describe = new DescribeInstancesRequest();
        describe.setPageSize(pageSize);
        DescribeInstancesResponse response = sampleDescribeInstancesResponse(regionId, describe, true);
        invoke(allEcs, response);
        //获取总数
        int totalCount = response.getTotalCount();
        // 循环次数
        int cnt = (totalCount + pageSize - 1) / pageSize;
        for (int i = 1; i < cnt; i++) {
            describe.setPageNumber(i + 1);
            response = sampleDescribeInstancesResponse(regionId, describe, true);
            invoke(allEcs, response);
        }
        return allEcs;
    }

    /**
     * ECS数据转EcsServerDO
     *
     * @param allEcs
     * @param response
     */
    private void invoke(List<EcsServerDO> allEcs, DescribeInstancesResponse response) {
        for (DescribeInstancesResponse.Instance i : response.getInstances()) {
            EcsServerDO ecs = new EcsServerDO(i);
            allEcs.add(ecs);
        }
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
        if (type == 1)
            return new BusinessWrapper<>(ecsSyncByAliyunFinance());
        return new BusinessWrapper<>(true);
    }


    private boolean ecsSyncByAliyun() {
        // 获取阿里云ECS
        List<EcsServerDO> servers = ecsGetAll();
        HashMap<String, ServerDO> serverMap = acqServerMapEcs();
        HashMap<String, EcsServerDO> ecsServerMap4Aliyun = new HashMap<>();
        try {
            for (EcsServerDO ecsServerDO : servers) {
                ecsServerMap4Aliyun.put(ecsServerDO.getInstanceId(), ecsServerDO);
                // server表已经存在
                if (serverMap.containsKey(ecsServerDO.getInsideIp())) {
                    ServerDO serverDO = serverMap.get(ecsServerDO.getInsideIp());
                    ecsServerDO.setContent(serverDO.getContent());
                    ecsServerDO.setServerId(serverDO.getId());
                    ecsServerDO.setStatus(EcsServerDO.statusAssociate);
                    //invokeDisk(ecsServerDO);
                    //更新公网ip
                    updateServerPublicIp(serverDO, ecsServerDO);
                    //在serverMap中移除
                    serverMap.remove(ecsServerDO.getInsideIp());
                } else {
                    // 新ECS服务器（server表不存在）
                    ecsServerDO.setStatus(EcsServerDO.statusNew);
                }
                invokeDisk(ecsServerDO);
                saveEcsServer(ecsServerDO);
            }
            // 处理server表中存在的但ECS中不存在的数据（ECS服务器下线）
            //System.err.println("处理server表中存在的但ECS中不存在的数据（ECS服务器下线）");
            for (Map.Entry<String, ServerDO> entry : serverMap.entrySet()) {
                ServerDO serverDO = entry.getValue();
                EcsServerDO ecsServerDO = serverDao.queryEcsByInsideIp(serverDO.getInsideIp());
                if (ecsServerDO == null || !ecsServerDO.isFinance()) {
                    ecsServerDO = new EcsServerDO(serverDO, EcsServerDO.statusOffline);
                    saveEcsServer(ecsServerDO);
                    //System.err.println(ecsServerDO);
                }
            }
            // 校验ECS表中多余的服务器（ECS服务器下线）
            // 获取ECS表中的 非金融云服务器 然后对比
            HashMap<String, EcsServerDO> ecsServerMap4DB = acqEcsServerMap4DB(false);
            for (Map.Entry<String, EcsServerDO> entry : ecsServerMap4Aliyun.entrySet()) {
                // 阿里云 API ECS
                EcsServerDO ecsServerDO = entry.getValue();
                // ECS如果存在则在map中移除
                if (ecsServerMap4DB.containsKey(ecsServerDO.getInstanceId()))
                    ecsServerMap4DB.remove(ecsServerDO.getInstanceId());
            }
            // 标记下线服务器
            for (Map.Entry<String, EcsServerDO> entry : ecsServerMap4DB.entrySet()) {
                EcsServerDO ecsServerDO = entry.getValue();
                ecsServerDO.setStatus(EcsServerDO.statusOffline);
                saveEcsServer(ecsServerDO);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean ecsSyncByAliyunFinance() {
        // 获取阿里云ECS
        List<EcsServerDO> servers = ecsFinanceGetAll();
        HashMap<String, ServerDO> serverMap = acqServerMapEcs();
        HashMap<String, EcsServerDO> ecsServerMap4Aliyun = new HashMap<>();
        try {
            // 遍历AliyunECS
            for (EcsServerDO ecsServerDO : servers) {
                ecsServerMap4Aliyun.put(ecsServerDO.getInstanceId(), ecsServerDO);
                // server表已经存在
                if (serverMap.containsKey(ecsServerDO.getInsideIp())) {
                    ServerDO serverDO = serverMap.get(ecsServerDO.getInsideIp());
                    ecsServerDO.setContent(serverDO.getContent());
                    ecsServerDO.setServerId(serverDO.getId());
                    ecsServerDO.setStatus(EcsServerDO.statusAssociate);
                    ecsServerDO.setFinance(true);
                    //invokeDisk(ecsServerDO);
                    //更新公网ip
                    updateServerPublicIp(serverDO, ecsServerDO);
                    //在serverMap中移除
                    serverMap.remove(ecsServerDO.getInsideIp());
                } else {
                    // 新ECS服务器（server表不存在）
                    ecsServerDO.setFinance(true);
                    ecsServerDO.setStatus(EcsServerDO.statusNew);
                }
                invokeDisk(ecsServerDO);
                saveEcsServer(ecsServerDO);
            }
            // 处理server表中存在的但ECS中不存在的数据（ECS服务器下线）
            //System.err.println("处理server表中存在的但ECS中不存在的数据（ECS服务器下线）");
            for (Map.Entry<String, ServerDO> entry : serverMap.entrySet()) {
                ServerDO serverDO = entry.getValue();
                EcsServerDO ecsServerDO = serverDao.queryEcsByInsideIp(serverDO.getInsideIp());
                if (ecsServerDO == null || ecsServerDO.isFinance()) {
                    ecsServerDO = new EcsServerDO(serverDO, EcsServerDO.statusOffline);
                    saveEcsServer(ecsServerDO);
                    // System.err.println(ecsServerDO);
                }
            }
            // 校验ECS表中多余的服务器（ECS服务器下线）
            // 获取ECS表中的 非金融云服务器 然后对比
            HashMap<String, EcsServerDO> ecsServerMap4DB = acqEcsServerMap4DB(true);
            for (Map.Entry<String, EcsServerDO> entry : ecsServerMap4Aliyun.entrySet()) {
                // 阿里云 API ECS
                EcsServerDO ecsServerDO = entry.getValue();
                // ECS如果存在则在map中移除
                if (ecsServerMap4DB.containsKey(ecsServerDO.getInstanceId()))
                    ecsServerMap4DB.remove(ecsServerDO.getInstanceId());
            }
            // 标记下线服务器
            for (Map.Entry<String, EcsServerDO> entry : ecsServerMap4DB.entrySet()) {
                EcsServerDO ecsServerDO = entry.getValue();
                ecsServerDO.setStatus(EcsServerDO.statusOffline);
                saveEcsServer(ecsServerDO);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;


    }

    /**
     * 从DB获取所有ECS的Map
     *
     * @return
     */
    private HashMap<String, EcsServerDO> acqEcsServerMap4DB(boolean finance) {
        HashMap<String, EcsServerDO> map = new HashMap<>();
        List<EcsServerDO> servers = serverDao.getEcsServerByFinance(finance);
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
     * 查询所有ecs状态
     *
     * @param regionId
     * @return
     */
    private List<EcsServerDO> ecsStatusAll(String regionId) {
        if (regionId == null) regionId = regionIdCnHangzhou;
        List<EcsServerDO> allEcs = new ArrayList<EcsServerDO>();
        int pageSize = 50;
        DescribeInstanceStatusRequest describe = new DescribeInstanceStatusRequest();
        describe.setPageSize(pageSize);
        if (regionId == null) regionId = regionIdCnHangzhou;
        DescribeInstanceStatusResponse response = sampleDescribeInstanceStatusResponse(regionId, describe);
        invoke(allEcs, response);
        int totalCount = response.getTotalCount();
        // 循环次数
        int cnt = (totalCount + pageSize - 1) / pageSize;
        for (int i = 1; i < cnt; i++) {
            describe.setPageNumber(i + 1);
            response = sampleDescribeInstanceStatusResponse(regionId, describe);
            invoke(allEcs, response);
        }
        return allEcs;
    }

    /**
     * 查询ecs状态
     *
     * @param regionId
     * @return
     */
    @Override
    public EcsServerDO ecsStatus(String regionId, ServerDO serverDO) {
        if (serverDO == null) return null;
        if (regionId == null) regionId = regionIdCnHangzhou;
        EcsServerDO ecs = ecsGet(regionId, serverDO);
        List<EcsServerDO> allEcs = ecsStatusAll(regionId);
        for (EcsServerDO i : allEcs) {
            if (i.getInstanceId().equals(ecs.getInstanceId())) {
                ecs.setStatus(i.getStatus());
                return ecs;
            }
        }
        return ecs;
    }


    /**
     * ECS数据转EcsServerDO
     *
     * @param allEcs
     * @param response
     */
    private void invoke(List<EcsServerDO> allEcs, DescribeInstanceStatusResponse response) {
        for (DescribeInstanceStatusResponse.InstanceStatus i : response.getInstanceStatuses()) {
            EcsServerDO ecs = new EcsServerDO(i.getInstanceId());
            allEcs.add(ecs);
        }
    }

    /**
     * 查询ecs
     *
     * @param regionId
     * @return
     */
    private EcsServerDO ecsGet(String regionId, ServerDO serverDO) {
        if (serverDO == null) return null;
        DescribeInstancesRequest describe = new DescribeInstancesRequest();
        if (regionId == null) regionId = regionIdCnHangzhou;
        describe.setRegionId(regionId);
        JSONArray ips = new JSONArray();
        ips.add(serverDO.getInsideIp());
        describe.setInnerIpAddresses(ips.toString());
        DescribeInstancesResponse response = sampleDescribeInstancesResponse(regionId, describe, false);
        return new EcsServerDO(response.getInstances().get(0));
    }

    @Override
    public EcsServerDO ecsGet(String regionId, String instanceId) {
        if (regionId == null) regionId = regionIdCnHangzhou;
        DescribeInstancesResponse.Instance ecs = query(regionId, instanceId);
        try {
            return new EcsServerDO(ecs);
        } catch (Exception e) {
            return new EcsServerDO();
        }
    }

    @Override
    public DescribeInstancesResponse.Instance query(String regionId, String instanceId) {
        DescribeInstancesRequest describe = new DescribeInstancesRequest();
        if (regionId == null) regionId = regionIdCnHangzhou;
        describe.setRegionId(regionId);
        JSONArray instanceIds = new JSONArray();
        instanceIds.add(instanceId);
        describe.setInstanceIds(instanceIds.toString());

        try {
            DescribeInstancesResponse response = sampleDescribeInstancesResponse(regionId, describe, false);
            DescribeInstancesResponse.Instance ecs = response.getInstances().get(0);
            return ecs;
        } catch (Exception e) {
            return new DescribeInstancesResponse.Instance();
        }
    }


    /**
     * 修改服务器名称
     *
     * @param regionId
     * @return
     */
    private boolean ecsModifyName(String regionId, ServerDO serverDO) {
        if (serverDO == null) return false;
        if (regionId == null) regionId = regionIdCnHangzhou;
        EcsServerDO ecs = ecsGet(regionId, serverDO);
        //ecs不存在
        if (ecs == null) return false;
        ModifyInstanceAttributeRequest describe = new ModifyInstanceAttributeRequest();
        //describe.setActionName("ModifyInstanceAttribute");
        describe.setInstanceId(ecs.getInstanceId());
        describe.setInstanceName(serverDO.acqServerName());
        ModifyInstanceAttributeResponse response = sampleModifyInstanceAttributeResponse(regionId, describe);
        if (response == null || response.getRequestId().isEmpty()) return false;
        return true;
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
        HashMap<String, String> configMap = acqConifMap();
        String aliyunAccessKey = configMap.get(AliyunEcsItemEnum.ALIYUN_ECS_ACCESS_KEY.getItemKey());
        String aliyunAccessSecret = configMap.get(AliyunEcsItemEnum.ALIYUN_ECS_ACCESS_SECRET.getItemKey());
        //生成 IClientProfile 的对象 profile，该对象存放 Access Key ID 和 Access Key Secret 和默认的地域信息
        IClientProfile profile = DefaultProfile.getProfile(regionId, aliyunAccessKey, aliyunAccessSecret);
        //创建一个对应方法的 Request，类的命名规则一般为 API 的方法名加上 “Request”，如获得镜像列表的 API 方法名为 DescribeImages，
        //那么对应的请求类名就是 DescribeImagesRequest，直接使用构造函数生成一个默认的类 describe：
        IAcsClient client = new DefaultAcsClient(profile);
        return client;
    }


    /**
     * 金融云
     *
     * @param regionId
     * @return
     */
    private IAcsClient acqIAcsFinanceClient(String regionId) {
        HashMap<String, String> configMap = acqConifMap();
        String aliyunFinanceAccessKey = configMap.get(AliyunEcsItemEnum.ALIYUN_FINANCE_ECS_ACCESS_KEY.getItemKey());
        String aliyunFinanceAccessSecret = configMap.get(AliyunEcsItemEnum.ALIYUN_FINANCE_ECS_ACCESS_SECRET.getItemKey());
        IClientProfile profile = DefaultProfile.getProfile(regionId, aliyunFinanceAccessKey, aliyunFinanceAccessSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        return client;
    }


    private DescribeInstancesResponse sampleDescribeInstancesResponse(String regionId, DescribeInstancesRequest describe, boolean finance) {
        if (regionId == null) regionId = regionIdCnHangzhou;
        IAcsClient client;
        if (finance) {
            client = acqIAcsFinanceClient(regionId);
        } else {
            client = acqIAcsClient(regionId);
        }
        try {
            DescribeInstancesResponse response
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
     * 修改ecs属性
     *
     * @param regionId
     * @param describe
     * @return
     */
    private ModifyInstanceAttributeResponse sampleModifyInstanceAttributeResponse(String regionId, ModifyInstanceAttributeRequest describe) {
        if (regionId == null) regionId = regionIdCnHangzhou;
        IAcsClient client = acqIAcsClient(regionId);
        try {
            ModifyInstanceAttributeResponse response
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
     * 查询ecs状态
     *
     * @param regionId
     * @param describe
     * @return
     */
    private DescribeInstanceStatusResponse sampleDescribeInstanceStatusResponse(String regionId, DescribeInstanceStatusRequest describe) {
        if (regionId == null) regionId = regionIdCnHangzhou;
        IAcsClient client = acqIAcsClient(regionId);
        try {
            DescribeInstanceStatusResponse response
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
        HashMap<String, String> configMap = acqConifMap();
        long networkId = Long.valueOf(configMap.get(AliyunEcsItemEnum.ALIYUN_ECS_PUBLIC_NETWORK_ID.getItemKey()));
        // ecs无公网ip
        if (StringUtils.isEmpty(ecsServerDO.getPublicIp())) return;
        // server有公网ip
        if (!StringUtils.isEmpty(serverDO.getPublicIp())) return;

        IPDetailDO publicDO = new IPDetailDO(networkId, ecsServerDO.getPublicIp(), IPDetailDO.publicIP);
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
            serverDao.delServerById(server.getId());
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
        List<DescribeDisksResponse.Disk> disks = queryDisks(ecsServerDO.getRegionId(), ecsServerDO.getInstanceId(), ecsServerDO.isFinance());
        try {
            for (DescribeDisksResponse.Disk disk : disks) {
                if (disk.getType() == DescribeDisksResponse.Disk.Type.SYSTEM) {
                    ecsServerDO.setSystemDiskCategory(disk.getCategory().getStringValue());
                    ecsServerDO.setSystemDiskSize(disk.getSize());
                    continue;
                }
                if (disk.getType() == DescribeDisksResponse.Disk.Type.DATA) {
                    if (disk.getCategory() != null && disk.getCategory().getStringValue() != null)
                        ecsServerDO.setDataDiskCategory(disk.getCategory().getStringValue());
                    ecsServerDO.setDataDiskSize(disk.getSize());
                    continue;
                }

               // System.err.println(disk.getType());
               // System.err.println(disk.getCategory().getStringValue());
            }

        } catch (Exception e) {
            e.printStackTrace();
            //System.err.println(ecsServerDO);

        }

    }

    public List<DescribeDisksResponse.Disk> queryDisks(String regionId, String instanceId, boolean isFinance) {
        if (regionId == null) regionId = regionIdCnHangzhou;
        DescribeDisksRequest request = new DescribeDisksRequest();
        request.setInstanceId(instanceId);
        DescribeDisksResponse response = sampleDescribeDisksResponse(regionId, request, isFinance);
        if (response == null || response.getRequestId().isEmpty()) return new ArrayList<DescribeDisksResponse.Disk>();
        return response.getDisks();
    }


    /**
     * 查询磁盘
     *
     * @param regionId
     * @param request
     * @return
     */
    private DescribeDisksResponse sampleDescribeDisksResponse(String regionId, DescribeDisksRequest request, boolean isFinance) {
        IAcsClient client;
        if (isFinance) {
            client = acqIAcsFinanceClient(regionId);
        } else {
            client = acqIAcsClient(regionId);
        }

        try {
            DescribeDisksResponse response
                    = client.getAcsResponse(request);
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
        DescribeInstancesResponse.Instance instance = this.query(ecsServerDO.getRegionId(), ecsServerDO.getInstanceId());
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
        } catch (Exception e) {

        }

    }


}
