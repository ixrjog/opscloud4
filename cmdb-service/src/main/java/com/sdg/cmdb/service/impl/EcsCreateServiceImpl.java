package com.sdg.cmdb.service.impl;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.sdg.cmdb.dao.cmdb.IPGroupDao;
import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.TableVO;

import com.sdg.cmdb.domain.aliyun.AliyunNetworkDO;
import com.sdg.cmdb.domain.aliyun.AliyunVO;

import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.ip.IPDetailDO;
import com.sdg.cmdb.domain.ip.IPDetailVO;
import com.sdg.cmdb.domain.ip.IPNetworkDO;
import com.sdg.cmdb.domain.server.*;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.util.PasswdUtils;
import com.sdg.cmdb.util.SessionUtils;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;

import java.util.List;

/**
 * Created by liangjian on 2017/4/18.
 */
@Service
public class EcsCreateServiceImpl implements EcsCreateService {

    private static final Logger logger = LoggerFactory.getLogger(EcsCreateServiceImpl.class);
    private static final Logger coreLogger = LoggerFactory.getLogger("coreLogger");


    @Value(value = "${aliyun.ecs.classic.security.group.id}")
    private String classicSecurityGroupId;


    @Value(value = "${aliyun.ecs.public.network.id}")
    private String publicNetworkId;

    @Resource
    private EcsService ecsService;

    @Resource
    private ServerGroupDao serverGroupDao;

    @Resource
    private ServerDao serverDao;

    @Resource
    private ServerService serverService;

    @Resource
    private ConfigService configService;

    @Autowired
    private AliyunService aliyunService;

    @Autowired
    private IPService ipService;

    @Autowired
    private IPGroupDao ipGroupDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private SchedulerManager schedulerManager;

    @Autowired
    private ServerGroupService serverGroupService;

    @Value("#{cmdb['invoke.env']}")
    private String invokeEnv;

    @Override
    public EcsTaskVO getEcsTask(long taskId) {
        EcsTaskDO ecsTaskDO = serverDao.getEcsTask(taskId);
        List<EcsTaskServerDO> taskServerList = serverDao.getEcsTaskServerByTaskId(taskId);
        List<EcsServerDO> serverList = new ArrayList<>();
        for (EcsTaskServerDO ecsTaskServerDO : taskServerList) {
            EcsServerDO ecsServerDO = serverDao.queryEcsByInstanceId(ecsTaskServerDO.getInstanceId());
            if (ecsServerDO == null)
                ecsServerDO = new EcsServerDO(ecsTaskServerDO.getInstanceId());
            serverList.add(ecsServerDO);
        }
        EcsTaskVO ecsTaskVO = new EcsTaskVO(ecsTaskDO, serverList);
        return ecsTaskVO;
    }

    @Override
    public EcsTaskDO createEcsTask(CreateEcsVO template) {
        String username = SessionUtils.getUsername();
        UserDO userDO = userDao.getUserByName(username);
        EcsTaskDO ecsTaskDO = new EcsTaskDO(userDO, template.getCnt());
        serverDao.addEcsTask(ecsTaskDO);
        ServerVO serverVO = template.getServerVO();
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(serverVO.getServerGroupDO().getId());
        // TODO 设置服务器名称
        if (StringUtils.isEmpty(serverVO.getServerName()))
            serverVO.setServerName(serverGroupDO.acqShortName());
        EcsTemplateDO ecsTemplateDO = serverDao.queryEcsTemplateById(template.getEcsTemplateId());
        template.setEcsTemplateDO(ecsTemplateDO);
        List<ServerDO> servers = serverDao.getServersByGroupIdAndEnvType(serverGroupDO.getId(), template.getServerVO().getEnvType());
        int serverSize = servers.size();
        schedulerManager.registerJob(() -> {
            createEcsTask(template, serverVO, serverSize, ecsTaskDO);
        });
        return ecsTaskDO;
    }

    private void createEcsTask(CreateEcsVO template, ServerVO serverVO, int serverSize, EcsTaskDO ecsTaskDO) {
        List<CreateInstanceResponse> instanceList = new ArrayList<>();
        List<EcsServerDO> newServers = new ArrayList<EcsServerDO>();
        // TODO 先开通服务器
        for (int i = 1; i <= template.getCnt(); i++) {
            //serverVO.setSerialNumber(String.valueOf(serverSize + i));
            template.getServerVO().setSerialNumber(String.valueOf(serverSize + i));
            CreateInstanceResponse instance = createEcs(null, template);
            instanceList.add(instance);
            // TODO 插入TaskServer记录，用于前端返回
            EcsTaskServerDO ecsTaskServerDO = new EcsTaskServerDO(ecsTaskDO.getId(), instance.getInstanceId());
            serverDao.addEcsTaskServer(ecsTaskServerDO);
        }
        // TODO 登记ECS实例信息
        for (CreateInstanceResponse instance : instanceList) {
            newServers.add(saveServer(instance.getInstanceId(), template));
        }
        // TODO 更新Task完成
        ecsTaskDO.setComplete(true);
        serverDao.updateEcsTask(ecsTaskDO);
        //变更配置
        if (invokeEnv.equalsIgnoreCase("online"))
            configService.invokeServerConfig(serverVO);
    }

    @Override
    public HttpResult create(String regionId, CreateEcsVO template) {
        if (regionId == null) regionId = EcsServiceImpl.regionIdCnHangzhou;
        ServerVO serverVO = template.getServerVO();
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(serverVO.getServerGroupDO().getId());
        if (serverGroupDO == null)
            return new HttpResult(new BusinessWrapper<>(ErrorCode.serverGroupNotExist));
        // 设置服务器名称
        if (StringUtils.isEmpty(serverVO.getServerName()))
            serverVO.setServerName(serverGroupDO.acqShortName());
        EcsTemplateDO ecsTemplateDO = serverDao.queryEcsTemplateById(template.getEcsTemplateId());
        template.setEcsTemplateDO(ecsTemplateDO);
        List<ServerDO> servers = serverDao.getServersByGroupIdAndEnvType(serverGroupDO.getId(), template.getServerVO().getEnvType());
        int serverSize = servers.size();

        List<EcsServerDO> newServers = new ArrayList<EcsServerDO>();
        List<CreateInstanceResponse> instanceList = new ArrayList<>();

        for (int i = 1; i <= template.getCnt(); i++) {
            //serverVO.setSerialNumber(String.valueOf(serverSize + i));
            template.getServerVO().setSerialNumber(String.valueOf(serverSize + i));
            CreateInstanceResponse instance = createEcs(regionId, template);
            instanceList.add(instance);
        }
        for (CreateInstanceResponse instance : instanceList) {
            newServers.add(saveServer(instance.getInstanceId(), template));
        }
        //变更配置
        if (invokeEnv.equalsIgnoreCase("online"))
            configService.invokeServerConfig(serverVO);

        List<EcsServerVO> resultServers = new ArrayList<EcsServerVO>();
        for (EcsServerDO ecsServerDO : newServers) {
            ServerDO serverDO = serverDao.queryServerByInsideIp(ecsServerDO.getInsideIp());
            resultServers.add(new EcsServerVO(ecsServerDO, serverDO));
        }
        return new HttpResult(new TableVO<>(resultServers.size(), resultServers));
    }

    private EcsServerDO saveServer(String instanceId, CreateEcsVO template, int retry) {
        for (int i = 1; i <= retry; i++) {
            try {
                Thread.sleep(2000);
                EcsServerDO ecsServerDO = saveServer(instanceId, template);
                if (!StringUtils.isEmpty(ecsServerDO.getInsideIp()))
                    return ecsServerDO;
            } catch (Exception e) {

            }
        }
        return new EcsServerDO();
    }

    /**
     * 新建的ecs保存server记录
     */
    private EcsServerDO saveServer(String instanceId, CreateEcsVO template) {
        try {
            ServerVO serverVO = template.getServerVO();
            // TODO 用于查询新录入服务器SerialNumber
            List<ServerDO> servers = serverDao.getServersByGroupIdAndEnvType(serverVO.getServerGroupDO().getId(), template.getServerVO().getEnvType());

            // EcsServerDO ecsServerDO = ecsService.ecsGet(null, instanceId);
            EcsServerDO ecsServerDO = ecsGet(instanceId);
            if (StringUtils.isEmpty(ecsServerDO.getInsideIp())) return ecsServerDO;
            IPDetailVO insideIP = serverVO.getInsideIP();
            insideIP.setIp(ecsServerDO.getInsideIp());
            serverVO.setInsideIP(insideIP);
            serverVO.setSerialNumber(String.valueOf(servers.size() + 1));
            IPDetailVO publicIP = serverVO.getPublicIP();
            publicIP.setIp(ecsServerDO.getPublicIp());
            serverVO.setPublicIP(publicIP);
            serverService.saveServer(serverVO);
            ecsService.updateEcsServerForServer(ecsServerDO);
            ecsService.saveEcsServerProperty(ecsServerDO);
            return ecsServerDO;
        } catch (Exception e) {
            return new EcsServerDO();
        }
    }


    private EcsServerDO ecsGet(String instanceId) {
        for (int i = 1; i <= 5; i++) {
            try {
                Thread.sleep(1000);
                EcsServerDO ecsServerDO = ecsService.ecsGet(null, instanceId);
                if (!StringUtils.isEmpty(ecsServerDO.getInsideIp()))
                    return ecsServerDO;
            } catch (Exception e) {
            }
        }
        return new EcsServerDO();
    }


    @Override
    public CreateInstanceResponse createEcs(String regionId, CreateEcsVO template) {
        if (regionId == null) regionId = EcsServiceImpl.regionIdCnHangzhou;

        ServerVO serverVO = template.getServerVO();
        AliyunVO aliyunVO = aliyunService.getAliyun(template);

        CreateInstanceRequest create = new CreateInstanceRequest();
        // 实例所属的可用区编号，空表示由系统选择，默认值：空。
        create.setZoneId(template.getEcsTemplateDO().getZoneId());
        // 镜像文件ID
        create.setImageId(aliyunVO.getAliyunEcsImageDO().getImageId());
        // 实例规格
        create.setInstanceType(template.getEcsTemplateDO().getInstanceType());
        // 配置实例网络
        if (template.getNetworkType().equalsIgnoreCase(AliyunNetworkDO.NetworkTypeEnum.vpc.getDesc())) {
            // vpc
            create.setVSwitchId(aliyunVO.getAliyunVswitchDO().getVswitchId());
            create.setSecurityGroupId(aliyunVO.getAliyunVpcSecurityGroupDO().getSecurityGroupId());
        } else {
            // classic
            create.setSecurityGroupId(classicSecurityGroupId);
        }
        // 设置付费类型
        create.setInstanceChargeType(template.getChargeType());
        if (template.getChargeType().equalsIgnoreCase("PrePaid"))
            create.setPeriod(template.getPeriod());

        // 设置部分属性
        invokeCreateInstanceRequest(create);

        create.setHostName(serverVO.acqServerName());

        /**
         * none：非 IO 优化
         optimized：IO 优化
         */
        create.setIoOptimized(template.getEcsTemplateDO().getIoOptimized());

        /**
         * cloud – 普通云盘
         cloud_efficiency – 高效云盘
         cloud_ssd – SSD云盘
         ephemeral_ssd - 本地 SSD 盘
         */
        create.setSystemDiskCategory(template.getEcsTemplateDO().getSystemDiskCategory());
        // TODO 设置系统盘大小 必须超过40
        if (template.getSystemDiskSize() > 40)
            create.setSystemDiskSize(template.getSystemDiskSize());

        if (template.getDataDiskSize() > 0) {
            CreateInstanceRequest.DataDisk dataDisk = new CreateInstanceRequest.DataDisk();
            dataDisk.setCategory(template.getEcsTemplateDO().getDataDisk1Category());
            dataDisk.setSize(template.getDataDiskSize());

            /**
             * 数据盘n的磁盘大小（n从1开始编号）。 以GB为单位，取值范围为：
             cloud：5 ~ 2000
             cloud_efficiency：20 ~ 32768
             cloud_ssd：20 ~ 32768
             ephemeral_ssd：5 ~ 800
             */
            List<CreateInstanceRequest.DataDisk> dataDiskList = new ArrayList<>();
            dataDiskList.add(dataDisk);
            create.setDataDisks(dataDiskList);

        }
        CreateInstanceResponse instance = sampleCreateInstanceResponse(regionId, create);

        if (instance == null || instance.getInstanceId().isEmpty()) return null;
        String instanceId = instance.getInstanceId();
        // 分配公网ip
        if (template.isAllocatePublicIpAddress()) {
            try {
                String publicIp = allocatePublicIpAddress(instanceId, 8);
            } catch (Exception e) {
            }
        }
        // 启动ecs
        ecsService.powerOn(instanceId);
        return instance;
    }

    private void invokeCreateInstanceRequest(CreateInstanceRequest create) {
        //网络计费类型，按流量计费还是按固定带宽计费。
        create.setInternetChargeType(AliyunNetworkDO.InternetChargeTypeEnum.traffic.getDesc());
        //公网入带宽最大值，单位为 Mbps
        create.setInternetMaxBandwidthIn(10);
        create.setInternetMaxBandwidthOut(10);
        /**
         * 实例的密码。8-30个字符，必须同时包含三项（大、小写字母，数字和特殊符号）。
         * 支持以下特殊字符：( ) ` ~ ! @ # $ % ^ & * - + = | { } [ ] : ; ‘ < > , . ? /
         */
        create.setPassword(PasswdUtils.getRandomPasswd(20) + "0Aa#");
    }


    private IAcsClient acqIAcsClient(String regionId) {
        return aliyunService.acqIAcsClient(regionId);
    }

    private CreateInstanceResponse sampleCreateInstanceResponse(String regionId, CreateInstanceRequest createInstanceRequest) {
        if (regionId == null) regionId = EcsServiceImpl.regionIdCnHangzhou;
        IAcsClient client = acqIAcsClient(regionId);
        try {
            CreateInstanceResponse response
                    = client.getAcsResponse(createInstanceRequest);
            return response;
        } catch (ServerException e) {
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean queryImages(String regionId, String imageName) {
        if (regionId == null) regionId = EcsServiceImpl.regionIdCnHangzhou;
        DescribeImagesRequest images = new DescribeImagesRequest();
        // 实例所属的可用区编号，空表示由系统选择，默认值：空。
        //images.setImageName(imageName);
        images.setImageOwnerAlias("self");
        DescribeImagesResponse response = sampleDescribeImagesResponse(regionId, images);
        for (int i = 0; i < response.getImages().size(); i++) {
            System.err.println(response.getImages().get(i).getImageId());
            System.err.println(response.getImages().get(i).getImageName());
        }

        if (response == null || response.getRequestId().isEmpty()) return false;
        return true;
    }

    private DescribeImagesResponse sampleDescribeImagesResponse(String regionId, DescribeImagesRequest describeImagesRequest) {
        if (regionId == null) regionId = EcsServiceImpl.regionIdCnHangzhou;
        IAcsClient client = acqIAcsClient(regionId);
        try {
            DescribeImagesResponse response
                    = client.getAcsResponse(describeImagesRequest);
            return response;
        } catch (ServerException e) {
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String allocatePublicIpAddress(String instanceId, int retry) {
        for (int i = 1; i <= retry; i++) {
            try {
                Thread.sleep(2000);
                String ip = allocatePublicIpAddress(instanceId);
                if (!StringUtils.isEmpty(ip))
                    return ip;
            } catch (Exception e) {
            }
        }
        return "";
    }

    public String allocatePublicIpAddress(String instanceId) {
        try {
            AllocatePublicIpAddressRequest allocateIp = new AllocatePublicIpAddressRequest();
            allocateIp.setInstanceId(instanceId);

            AllocatePublicIpAddressResponse response = sampleAllocatePublicIpAddress(allocateIp);
            if (response == null || response.getIpAddress().isEmpty()) return "";
            return response.getIpAddress();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 分配ecs公网ip
     *
     * @param allocatePublicIpAddressRequest
     * @return
     */
    private AllocatePublicIpAddressResponse sampleAllocatePublicIpAddress(AllocatePublicIpAddressRequest allocatePublicIpAddressRequest) {
        IAcsClient client = acqIAcsClient(EcsServiceImpl.regionIdCnHangzhou);
        try {
            AllocatePublicIpAddressResponse response
                    = client.getAcsResponse(allocatePublicIpAddressRequest);
            return response;
        } catch (ServerException e) {
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            //  e.printStackTrace();
            logger.info("查询ECS服务器公网IP失败（会重试），失败原因可能服务器未启动！");
            return null;
        }
    }

    @Override
    public BusinessWrapper<Boolean> allocateIpAddress(String instanceId) {
        if (StringUtils.isEmpty(instanceId)) return new BusinessWrapper<>(ErrorCode.serverFailure);
        EcsServerDO ecsServerDO = serverDao.queryEcsByInstanceId(instanceId);
        if (ecsServerDO == null) return new BusinessWrapper<>(ErrorCode.serverFailure);
        ServerDO serverDO = serverDao.getServerInfoById(ecsServerDO.getServerId());
        String publicIp = this.allocatePublicIpAddress(instanceId);
        if (serverDO != null) {
            if (!isAllocateIp(instanceId))
                return new BusinessWrapper<>(ErrorCode.ecsStatusError);
            // String publicNetworkId
            long networkId = Long.valueOf(publicNetworkId);

            IPNetworkDO ipNetworkDO = ipGroupDao.queryIPGroupInfo(networkId);
            IPDetailVO publicIpVO = new IPDetailVO(publicIp, ipNetworkDO);
            ServerGroupDO serverGroupDO = serverGroupService.queryServerGroupById(serverDO.getServerGroupId());
            IPDetailVO insideIP = ipService.getIPDetail(new IPDetailDO(serverDO.getInsideIpId()));
            ServerVO serverVO = new ServerVO(serverDO, serverGroupDO, publicIpVO, insideIP);
            serverService.saveServer(serverVO);
        }
        //保存ecs表中的公网ip
        ecsServerDO.setPublicIp(publicIp);
        serverDao.updateEcsServer(ecsServerDO);
        return new BusinessWrapper<>(true);
    }

    /**
     * 判断是否允许新增ip  (只有开机或关机状态才可以新增ip)
     *
     * @param instanceId
     * @return
     */
    private boolean isAllocateIp(String instanceId) {
//        DescribeInstancesResponse.Instance instance = ecsService.query(null, instanceId);
//        DescribeInstancesResponse.Instance.
//        if (instance.getStatus().equalsIgnoreCase(DescribeInstancesResponse.Instance.Status.STOPPED.getStringValue()))
//            return true;
//        if (instance.getStatus().equalsIgnoreCase(DescribeInstancesResponse.Instance.Status.RUNNING.getStringValue()))
//            return true;
//        return false;
        return true;
    }


}
