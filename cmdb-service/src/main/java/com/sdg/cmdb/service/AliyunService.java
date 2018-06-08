package com.sdg.cmdb.service;

import com.aliyuncs.ecs.model.v20140526.DescribeImagesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeRegionsResponse;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.aliyun.*;
import com.sdg.cmdb.domain.server.CreateEcsVO;
import com.sdg.cmdb.domain.server.EcsServerDO;

import java.util.List;

/**
 * Created by liangjian on 2017/6/12.
 */
public interface AliyunService {

    List<AliyunEcsImageVO> queryAliyunImage(String queryDesc);

    BusinessWrapper<Boolean> saveAliyunImage(AliyunEcsImageDO aliyunEcsImageDO);

    BusinessWrapper<Boolean> delAliyunImage(long id);


    List<AliyunNetworkDO> getAliyunNetwork();

    List<AliyunVpcVO> queryAliyunVpc(String networkType, String queryDesc);

    /**
     * 查询Vpc详情
     * @return
     */
    List<AliyunVpcVO> getAliyunVpc();

    BusinessWrapper<Boolean> delAliyunVpc(long id);

    List<AliyunVswitchVO> queryAliyunVswitch(long vpcId, String queryDesc);

    List<AliyunVpcSecurityGroupVO> querySecurityGroup(long vpcId, String queryDesc);

    AliyunVO getAliyun(CreateEcsVO template);

    List<DescribeRegionsResponse.Region> getDescribeRegions();

    List<DescribeImagesResponse.Image> getDescribeImages();

    BusinessWrapper<Boolean> rsyncAliyunNetwork();

}
