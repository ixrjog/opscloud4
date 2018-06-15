package com.sdg.cmdb.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.sdg.cmdb.dao.cmdb.IPGroupDao;
import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.TableVO;

import com.sdg.cmdb.domain.aliyun.AliyunNetworkDO;
import com.sdg.cmdb.domain.aliyun.AliyunVO;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.AliyunEcsItemEnum;
import com.sdg.cmdb.domain.ip.IPDetailDO;
import com.sdg.cmdb.domain.ip.IPDetailVO;
import com.sdg.cmdb.domain.ip.IPNetworkDO;
import com.sdg.cmdb.domain.server.*;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.util.PasswdUtils;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liangjian on 2017/4/18.
 */
@Service
public class EcsCreateServiceImpl implements EcsCreateService {

    private static final Logger logger = LoggerFactory.getLogger(EcsCreateServiceImpl.class);
    private static final Logger coreLogger = LoggerFactory.getLogger("coreLogger");

    @Resource
    private AliyunService aliyunService;

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

    @Resource
    private ConfigCenterService configCenterService;

    @Resource
    private IPService ipService;

    @Resource
    private IPGroupDao ipGroupDao;

    @Resource
    private SchedulerManager schedulerManager;

    @Resource
    private ServerGroupService serverGroupService;

    private HashMap<String, String> configMap;

    @Value("#{cmdb['invoke.env']}")
    private String invokeEnv;

    private HashMap<String, String> acqConifMap() {
        if (configMap != null) return configMap;
        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.ALIYUN_ECS.getItemKey());
    }

    @Override
    public HttpResult create(String regionId, CreateEcsVO template) {
        // TableVO<List<EcsServerVO>>

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
        //开通ECS & 保存服务器信息（server & ecsServer）
        for (int i = 1; i <= template.getCnt(); i++) {
            serverVO.setSerialNumber(String.valueOf(serverSize + i));
            CreateInstanceResponse instance = createEcs(regionId, template);
            //instanceList.add(instance);
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
            EcsServerDO ecsServerDO = ecsService.ecsGet(null, instanceId);
            if (StringUtils.isEmpty(ecsServerDO.getInsideIp())) return ecsServerDO;
            IPDetailVO insideIP = serverVO.getInsideIP();
            insideIP.setIp(ecsServerDO.getInsideIp());
            serverVO.setInsideIP(insideIP);
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


    @Override
    public CreateInstanceResponse createEcs(String regionId, CreateEcsVO template) {
        if (regionId == null) regionId = EcsServiceImpl.regionIdCnHangzhou;

        ServerVO serverVO = template.getServerVO();

        AliyunVO aliyunVO = aliyunService.getAliyun(template);

        HashMap<String, String> configMap = acqConifMap();
        //String imageId = configMap.get(AliyunEcsItemEnum.ALIYUN_ECS_IMAGE_ID.getItemKey());
        String securityGroupId = configMap.get(AliyunEcsItemEnum.ALIYUN_ECS_SECURITY_GROUP_ID.getItemKey());
        //String vSwitchId = configMap.get(AliyunEcsItemEnum.ALIYUN_ECS_VSWITCH_ID.getItemKey());

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
            create.setSecurityGroupId(securityGroupId);
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

        if (template.getEcsTemplateDO().getDataDiskSize() > 0) {
            create.setDataDisk1Category(template.getEcsTemplateDO().getDataDisk1Category());
            /**
             * 数据盘n的磁盘大小（n从1开始编号）。 以GB为单位，取值范围为：
             cloud：5 ~ 2000
             cloud_efficiency：20 ~ 32768
             cloud_ssd：20 ~ 32768
             ephemeral_ssd：5 ~ 800
             */
            create.setDataDisk1Size(template.getEcsTemplateDO().getDataDiskSize());
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
            e.printStackTrace();
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
            HashMap<String, String> configMap = acqConifMap();
            String publicNetworkId = configMap.get(AliyunEcsItemEnum.ALIYUN_ECS_PUBLIC_NETWORK_ID.getItemKey());
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
        DescribeInstancesResponse.Instance instance = ecsService.query(null, instanceId);
        if (instance.getStatus().getStringValue().equalsIgnoreCase(DescribeInstancesResponse.Instance.Status.STOPPED.getStringValue()))
            return true;
        if (instance.getStatus().getStringValue().equalsIgnoreCase(DescribeInstancesResponse.Instance.Status.RUNNING.getStringValue()))
            return true;
        return false;
    }


}
