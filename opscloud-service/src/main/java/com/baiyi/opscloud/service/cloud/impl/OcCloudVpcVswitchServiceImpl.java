package com.baiyi.opscloud.service.cloud.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudVpcVswitch;
import com.baiyi.opscloud.domain.param.cloud.CloudVPCVSwitchParam;
import com.baiyi.opscloud.mapper.opscloud.OcCloudVpcVswitchMapper;
import com.baiyi.opscloud.service.cloud.OcCloudVpcVswitchService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
    public  DataTable<OcCloudVpcVswitch> queryOcCloudVPCVswitchByParam(CloudVPCVSwitchParam.PageQuery pageQuery){
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcCloudVpcVswitch> list = ocCloudVpcVswitchMapper.queryOcCloudVPCSecurityGroupByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public List<OcCloudVpcVswitch> queryOcCloudVpcVswitchByVpcId(String vpcId) {
        return ocCloudVpcVswitchMapper.selectByExample(getExampleByVpcId(vpcId));
    }

    @Override
    public List<OcCloudVpcVswitch> queryOcCloudVpcVswitchByVpcIdAndZoneIds(String vpcId, List<String> zoneIds) {
        return ocCloudVpcVswitchMapper.queryOcCloudVpcVswitchByVpcIdAndZoneIds(vpcId, zoneIds);
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

    @Override
    public void updateOcCloudVpcVswitch(OcCloudVpcVswitch ocCloudVpcVswitch) {
        ocCloudVpcVswitchMapper.updateByPrimaryKey(ocCloudVpcVswitch);
    }

    private Example getExampleByVpcId(String vpcId) {
        Example example = new Example(OcCloudVpcVswitch.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("vpcId", vpcId);
        return example;
    }

}
