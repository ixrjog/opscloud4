package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.aliyun.*;
import com.sdg.cmdb.domain.server.ServerDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by liangjian on 2017/6/12.
 */
@Component
public interface AliyunDao {

    /**
     * 查询阿里云ecs镜像
     *
     * @return
     */
    List<AliyunEcsImageDO> getEcsImage(@Param("queryDesc") String queryDesc);

    AliyunEcsImageDO queryEcsImageById(@Param("id") long id);

    int addEcsImage(AliyunEcsImageDO aliyunEcsImageDO);

    int delEcsImage(@Param("id") long id);

    /**
     * 查询EcsImage详情页
     *
     * @return
     */
    List<AliyunEcsImageDO> getEcsImagePage();

    /**
     * 查询阿里云网络类型
     *
     * @return
     */
    List<AliyunNetworkDO> getNetwork();

    /**
     * 查询阿里云网络类型
     *
     * @return
     */
    AliyunNetworkDO getNetworkByType(@Param("networkType") String networkType);


    /**
     * 查询阿里云vpc网络列表
     *
     * @param queryDesc
     * @return
     */
    List<AliyunVpcDO> queryAliyunVpc(@Param("networkId") long networkId,
                                     @Param("queryDesc") String queryDesc);

    AliyunVpcDO getAliyunVpc(@Param("id") long id);

    int delAliyunVpc(@Param("id") long id);

    AliyunVpcDO getAliyunVpcByAliyunVpcId(@Param("aliyunVpcId") String aliyunVpcId);

    int addAliyunVpc(AliyunVpcDO aliyunVpcDO);

    AliyunVpcDO queryAliyunVpcById(@Param("id") long id);

    /**
     * 查询阿里云vpc下的虚拟交换机
     *
     * @param vpcId
     * @param queryDesc
     * @return
     */
    List<AliyunVswitchDO> queryAliyunVswitch(
            @Param("vpcId") long vpcId,
            @Param("queryDesc") String queryDesc
    );

    AliyunVswitchDO queryAliyunVswitchById(@Param("id") long id);

    int delAliyunVswitch(@Param("id") long id);

    int addAliyunVswitch(AliyunVswitchDO aliyunVswitchDO);

    /**
     * 查询vpc下的安全组
     *
     * @param vpcId
     * @param queryDesc
     * @return
     */
    List<AliyunVpcSecurityGroupDO> querySecurityGroup(
            @Param("vpcId") long vpcId,
            @Param("queryDesc") String queryDesc
    );

    AliyunVpcSecurityGroupDO querySecurityGroupById(@Param("id") long id);

    int addSecurityGroup(AliyunVpcSecurityGroupDO aliyunVpcSecurityGroupDO);

    int delSecurityGroup(@Param("id") long id);

}
