package com.sdg.cmdb.service.impl;


import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.sdg.cmdb.dao.cmdb.AliyunDao;
import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.aliyun.*;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.AliyunEcsItemEnum;
import com.sdg.cmdb.domain.server.CreateEcsVO;
import com.sdg.cmdb.domain.server.EcsTemplateDO;
import com.sdg.cmdb.service.AliyunService;
import com.sdg.cmdb.service.ConfigCenterService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liangjian on 2017/6/13.
 */
@Service
public class AliyunServiceImpl implements AliyunService {

    @Resource
    private AliyunDao aliyunDao;

    @Resource
    private ServerDao serverDao;

    @Resource
    private ConfigCenterService configCenterService;

    private HashMap<String, String> configMap;


    private HashMap<String, String> acqConifMap() {
        if (configMap != null) return configMap;
        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.ALIYUN_ECS.getItemKey());
    }

    @Override
    public List<AliyunEcsImageVO> queryAliyunImage(String queryDesc) {
        List<AliyunEcsImageDO> list = aliyunDao.getEcsImage(queryDesc);
        List<AliyunEcsImageVO> listVO = new ArrayList<>();
        for (AliyunEcsImageDO aliyunEcsImageDO : list)
            listVO.add(new AliyunEcsImageVO(aliyunEcsImageDO));
        return listVO;
    }

    @Override
    public BusinessWrapper<Boolean> saveAliyunImage(AliyunEcsImageDO aliyunEcsImageDO) {
        try {
            aliyunDao.addEcsImage(aliyunEcsImageDO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delAliyunImage(long id) {
        try {
            aliyunDao.delEcsImage(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }


    @Override
    public List<AliyunNetworkDO> getAliyunNetwork() {
        return aliyunDao.getNetwork();
    }

    @Override
    public List<AliyunVpcVO> queryAliyunVpc(String networkType, String queryDesc) {
        if (StringUtils.isEmpty(networkType))
            networkType = "vpc";
        AliyunNetworkDO aliyunNetworkDO = aliyunDao.getNetworkByType(networkType);
        List<AliyunVpcDO> list = aliyunDao.queryAliyunVpc(aliyunNetworkDO.getId(), queryDesc);
        List<AliyunVpcVO> listVO = new ArrayList<>();
        for (AliyunVpcDO aliyunVpcDO : list)
            listVO.add(new AliyunVpcVO(aliyunVpcDO));
        return listVO;
    }

    @Override
    public List<AliyunVpcVO> getAliyunVpc() {
        List<AliyunVpcDO> list = aliyunDao.queryAliyunVpc(AliyunNetworkDO.NETWORK_VPC, "");
        List<AliyunVpcVO> voList = new ArrayList<>();
        for (AliyunVpcDO aliyunVpcDO : list) {
            List<AliyunVswitchDO> vSwitchs = aliyunDao.queryAliyunVswitch(aliyunVpcDO.getId(), "");
            List<AliyunVpcSecurityGroupDO> securityGroups = aliyunDao.querySecurityGroup(aliyunVpcDO.getId(), "");
            AliyunVpcVO aliyunVpcVO = new AliyunVpcVO(aliyunVpcDO, vSwitchs, securityGroups);
            voList.add(aliyunVpcVO);
        }
        return voList;
    }

    @Override
    public BusinessWrapper<Boolean> delAliyunVpc(long id) {
        try {
            AliyunVpcDO aliyunVpcDO = aliyunDao.getAliyunVpc(id);
            delVpcVSwitchs(aliyunVpcDO);
            delVpcSecurityGroups(aliyunVpcDO);
            aliyunDao.delAliyunVpc(aliyunVpcDO.getId());
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }


    @Override
    public List<AliyunVswitchVO> queryAliyunVswitch(long vpcId, String queryDesc) {
        List<AliyunVswitchDO> list = aliyunDao.queryAliyunVswitch(vpcId, queryDesc);
        List<AliyunVswitchVO> listVO = new ArrayList<>();
        for (AliyunVswitchDO aliyunVswitchDO : list)
            listVO.add(new AliyunVswitchVO(aliyunVswitchDO));
        return listVO;
    }

    @Override
    public List<AliyunVpcSecurityGroupVO> querySecurityGroup(long vpcId, String queryDesc) {
        List<AliyunVpcSecurityGroupDO> list = aliyunDao.querySecurityGroup(vpcId, queryDesc);
        List<AliyunVpcSecurityGroupVO> listVO = new ArrayList<>();
        for (AliyunVpcSecurityGroupDO aliyunVpcSecurityGroupDO : list)
            listVO.add(new AliyunVpcSecurityGroupVO(aliyunVpcSecurityGroupDO));
        return listVO;
    }


    @Override
    public AliyunVO getAliyun(CreateEcsVO template) {
        AliyunVO aliyunVO = new AliyunVO();
        aliyunVO.setAliyunEcsImageDO(aliyunDao.queryEcsImageById(template.getImageId()));
        if (!StringUtils.isEmpty(template.getNetworkType()) && template.getNetworkType().equalsIgnoreCase("vpc")) {
            aliyunVO.setAliyunVpcDO(aliyunDao.queryAliyunVpcById(template.getVpcId()));
            aliyunVO.setAliyunVswitchDO(aliyunDao.queryAliyunVswitchById(template.getVswitchId()));
            aliyunVO.setAliyunVpcSecurityGroupDO(aliyunDao.querySecurityGroupById(template.getSecurityGroupId()));
        }
        return aliyunVO;
    }

    @Override
    public List<DescribeRegionsResponse.Region> getDescribeRegions() {
        DescribeRegionsRequest describe = new DescribeRegionsRequest();
        try {
            DescribeRegionsResponse response = sampleDescribeRegionsResponse(describe);
            return response.getRegions();
        } catch (Exception e) {
            return new ArrayList<DescribeRegionsResponse.Region>();
        }
    }

    private DescribeRegionsResponse sampleDescribeRegionsResponse(DescribeRegionsRequest describe) {
        IAcsClient client = acqIAcsClient(EcsServiceImpl.regionIdCnHangzhou);
        try {
            DescribeRegionsResponse response
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

    @Override
    public List<DescribeImagesResponse.Image> getImages() {
        List<DescribeImagesResponse.Image> images = new ArrayList<DescribeImagesResponse.Image>();
        for (String regionId : acqRegionIds()) {
            try {
                DescribeImagesRequest describe = new DescribeImagesRequest();
                describe.setRegionId(regionId);
                // self：您创建的自定义镜像。
                describe.setImageOwnerAlias("self");
                describe.setPageSize(50);
                DescribeImagesResponse response = getImages(describe, regionId);
                images.addAll(response.getImages());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return images;
    }

    /**
     * 查询实例规格
     *
     * @param regionId
     * @return
     */
    @Override
    public List<DescribeInstanceTypesResponse.InstanceType> getInstanceTypes(String regionId) {
        if (StringUtils.isEmpty(regionId)) regionId = EcsServiceImpl.regionIdCnHangzhou;

        List<DescribeInstanceTypesResponse> types = new ArrayList<DescribeInstanceTypesResponse>();
        try {
            DescribeInstanceTypesRequest describe = new DescribeInstanceTypesRequest();
            describe.setRegionId(regionId);
            DescribeInstanceTypesResponse response = getInstanceTypes(describe, regionId);
            return response.getInstanceTypes();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<DescribeInstanceTypesResponse.InstanceType>();
        }

    }


    private DescribeInstanceTypesResponse getInstanceTypes(DescribeInstanceTypesRequest describe, String regionId) {
        IAcsClient client = acqIAcsClient(regionId);
        try {
            DescribeInstanceTypesResponse response
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


    @Override
    public List<DescribeZonesResponse.Zone> getZones(String regionId) {
        if (StringUtils.isEmpty(regionId)) regionId = EcsServiceImpl.regionIdCnHangzhou;
        try {
            DescribeZonesRequest describe = new DescribeZonesRequest();
            describe.setRegionId(regionId);
            DescribeZonesResponse response = getZones(describe, regionId);
            return response.getZones();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<DescribeZonesResponse.Zone>();
        }
    }

    private DescribeZonesResponse getZones(DescribeZonesRequest describe, String regionId) {
        IAcsClient client = acqIAcsClient(regionId);
        try {
            DescribeZonesResponse response
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
     * 同步阿里云网络配置，并入库
     *
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> rsyncAliyunNetwork() {
        List<DescribeVpcsResponse.Vpc> vpcs = getDescribeVpcs();
        for (DescribeVpcsResponse.Vpc vpc : vpcs) {
            AliyunVpcDO vpcDO = aliyunDao.getAliyunVpcByAliyunVpcId(vpc.getVpcId());
            if (vpcDO == null) {
                vpcDO = new AliyunVpcDO(vpc);
                aliyunDao.addAliyunVpc(vpcDO);
            }
            addVpcVSwitchs(vpcDO, vpc);
            addVpcSecurityGroups(vpcDO, vpc);
        }
        return new BusinessWrapper<Boolean>(true);
    }


    @Override
    public BusinessWrapper<Boolean> saveTemplate(EcsTemplateDO ecsTemplateDO) {
        try {
            serverDao.addEcsTemplate(ecsTemplateDO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delTemplate(long id) {
        try {
            serverDao.delEcsTemplate(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }


    /**
     * 插入vpc下的所有vSwitchs
     *
     * @param aliyunVpcDO
     * @param vpc
     */
    private void addVpcVSwitchs(AliyunVpcDO aliyunVpcDO, DescribeVpcsResponse.Vpc vpc) {
        DescribeVSwitchesRequest describe = new DescribeVSwitchesRequest();
        describe.setRegionId(vpc.getRegionId());
        describe.setVpcId(vpc.getVpcId());
        describe.setPageSize(50);
        DescribeVSwitchesResponse response = sampleDescribeVSwitchesResponse(describe, vpc.getRegionId());
        delVpcVSwitchs(aliyunVpcDO);
        for (DescribeVSwitchesResponse.VSwitch vS : response.getVSwitches()) {
            AliyunVswitchDO aliyunVswitchDO = new AliyunVswitchDO(aliyunVpcDO, vS);
            try {
                aliyunDao.addAliyunVswitch(aliyunVswitchDO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 插入vpc下的所有vSwitchs
     *
     * @param aliyunVpcDO
     * @param vpc
     */
    private void addVpcSecurityGroups(AliyunVpcDO aliyunVpcDO, DescribeVpcsResponse.Vpc vpc) {
        DescribeSecurityGroupsRequest describe = new DescribeSecurityGroupsRequest();
        describe.setRegionId(vpc.getRegionId());
        describe.setVpcId(vpc.getVpcId());
        describe.setPageSize(50);
        DescribeSecurityGroupsResponse response = sampleDescribeSecurityGroupsResponse(describe, vpc.getRegionId());
        delVpcSecurityGroups(aliyunVpcDO);
        for (DescribeSecurityGroupsResponse.SecurityGroup securityGroup : response.getSecurityGroups()) {
            AliyunVpcSecurityGroupDO aliyunVpcSecurityGroupDO = new AliyunVpcSecurityGroupDO(aliyunVpcDO, securityGroup);
            try {
                aliyunDao.addSecurityGroup(aliyunVpcSecurityGroupDO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除Vpc下的所有securityGroups
     *
     * @param aliyunVpcDO
     */
    private void delVpcSecurityGroups(AliyunVpcDO aliyunVpcDO) {
        try {
            List<AliyunVpcSecurityGroupDO> list = aliyunDao.querySecurityGroup(aliyunVpcDO.getId(), "");

            for (AliyunVpcSecurityGroupDO aliyunVpcSecurityGroupDO : list) {
                aliyunDao.delSecurityGroup(aliyunVpcSecurityGroupDO.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除Vpc下的所有vSwitchs
     *
     * @param aliyunVpcDO
     */
    private void delVpcVSwitchs(AliyunVpcDO aliyunVpcDO) {
        try {
            List<AliyunVswitchDO> list = aliyunDao.queryAliyunVswitch(aliyunVpcDO.getId(), "");
            for (AliyunVswitchDO aliyunVswitchDO : list) {
                aliyunDao.delAliyunVswitch(aliyunVswitchDO.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<DescribeVpcsResponse.Vpc> getDescribeVpcs() {
        List<DescribeVpcsResponse.Vpc> vpcs = new ArrayList<DescribeVpcsResponse.Vpc>();
        for (String regionId : acqRegionIds()) {
            try {
                DescribeVpcsRequest describe = new DescribeVpcsRequest();
                describe.setRegionId(regionId);
                describe.setPageSize(50);
                DescribeVpcsResponse response = sampleDescribeVpcsResponse(describe, regionId);
                vpcs.addAll(response.getVpcs());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return vpcs;
    }


    /**
     * 查询安全组
     *
     * @param describe
     * @param regionId
     * @return
     */
    private DescribeSecurityGroupsResponse sampleDescribeSecurityGroupsResponse(DescribeSecurityGroupsRequest describe, String regionId) {
        IAcsClient client = acqIAcsClient(regionId);
        try {
            DescribeSecurityGroupsResponse response
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
     * 查询阿里云Vpc网络
     *
     * @param describe
     * @param regionId
     * @return
     */
    private DescribeVpcsResponse sampleDescribeVpcsResponse(DescribeVpcsRequest describe, String regionId) {
        IAcsClient client = acqIAcsClient(regionId);
        try {
            DescribeVpcsResponse response
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
     * 查询阿里云VSwitches
     *
     * @param describe
     * @param regionId
     * @return
     */
    private DescribeVSwitchesResponse sampleDescribeVSwitchesResponse(DescribeVSwitchesRequest describe, String regionId) {
        IAcsClient client = acqIAcsClient(regionId);
        try {
            DescribeVSwitchesResponse response
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
     * 查询阿里云镜像
     *
     * @param describe
     * @param regionId
     * @return
     */
    private DescribeImagesResponse getImages(DescribeImagesRequest describe, String regionId) {
        IAcsClient client = acqIAcsClient(regionId);
        try {
            DescribeImagesResponse response
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
     * 重构
     *
     * @param regionId
     * @return
     */
    private IAcsClient acqIAcsClient(String regionId) {
        HashMap<String, String> configMap = acqConifMap();
        String accessKey = configMap.get(AliyunEcsItemEnum.ALIYUN_ECS_ACCESS_KEY.getItemKey());
        String accessSecret = configMap.get(AliyunEcsItemEnum.ALIYUN_ECS_ACCESS_SECRET.getItemKey());
        IClientProfile profile = DefaultProfile.getProfile(regionId, accessKey, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        return client;
    }

    /**
     * 获取配置的所有regionId
     *
     * @return
     */
    private String[] acqRegionIds() {
        HashMap<String, String> configMap = acqConifMap();
        String aliyunRegionId = configMap.get(AliyunEcsItemEnum.ALIYUN_ECS_REGION_ID.getItemKey());
        String[] regionIds = aliyunRegionId.split(",");
        return regionIds;
    }


}
