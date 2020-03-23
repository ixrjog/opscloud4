package com.baiyi.opscloud.service.cloud.impl;

import com.baiyi.opscloud.domain.generator.OcCloudVpcVswitch;
import com.baiyi.opscloud.mapper.opscloud.OcCloudVpcVswitchMapper;
import com.baiyi.opscloud.service.cloud.OcCloudVpcVswitchService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/19 9:58 上午
 * @Version 1.0
 */
@Service
public class OcCloudVpcVswitchServiceImpl implements OcCloudVpcVswitchService {

    @Resource
    private OcCloudVpcVswitchMapper ocCloudVpcVswitchMapper;

    @Override
    public void deleteOcCloudVpcVswitchByVpcId(String vpcId) {
        ocCloudVpcVswitchMapper.deleteByExample(getExampleByVpcId(vpcId));
    }

    @Override
    public List<OcCloudVpcVswitch> queryOcCloudVpcVswitchByVpcId(String vpcId) {
        return ocCloudVpcVswitchMapper.selectByExample(getExampleByVpcId(vpcId));
    }

    @Override
    public OcCloudVpcVswitch queryOcCloudVpcVswitchById(int id) {
        return ocCloudVpcVswitchMapper.selectByPrimaryKey(id);
    }

    @Override
    public OcCloudVpcVswitch queryOcCloudVpcVswitchByVswitchId(String vswitchId) {
        Example example = new Example(OcCloudVpcVswitch.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("vswitchId", vswitchId);
        return ocCloudVpcVswitchMapper.selectOneByExample(example);
    }

    @Override
    public void deleteOcCloudVpcVswitchById(int id) {
        ocCloudVpcVswitchMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void addOcCloudVpcVswitch(OcCloudVpcVswitch ocCloudVpcVswitch) {
        ocCloudVpcVswitchMapper.insert(ocCloudVpcVswitch);
    }

    private Example getExampleByVpcId(String vpcId) {
        Example example = new Example(OcCloudVpcVswitch.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("vpcId", vpcId);
        return example;
    }

}