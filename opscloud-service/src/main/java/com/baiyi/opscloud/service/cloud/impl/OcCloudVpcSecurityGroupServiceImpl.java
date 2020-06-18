package com.baiyi.opscloud.service.cloud.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudVpcSecurityGroup;
import com.baiyi.opscloud.domain.param.cloud.CloudVPCSecurityGroupParam;
import com.baiyi.opscloud.mapper.opscloud.OcCloudVpcSecurityGroupMapper;
import com.baiyi.opscloud.service.cloud.OcCloudVpcSecurityGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/19 1:16 下午
 * @Version 1.0
 */
@Service
public class OcCloudVpcSecurityGroupServiceImpl implements OcCloudVpcSecurityGroupService {

    @Resource
    private OcCloudVpcSecurityGroupMapper ocCloudVpcSecurityGroupMapper;

    @Override
    public DataTable<OcCloudVpcSecurityGroup> queryOcCloudVPCSecurityGroupByParam(CloudVPCSecurityGroupParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcCloudVpcSecurityGroup> ocCloudVpcSecurityGroupList = ocCloudVpcSecurityGroupMapper.queryOcCloudVPCSecurityGroupByParam(pageQuery);
        return new DataTable<>(ocCloudVpcSecurityGroupList, page.getTotal());
    }

    @Override
    public void deleteOcCloudVpcSecurityGroupByVpcId(String vpcId) {
        ocCloudVpcSecurityGroupMapper.deleteByExample(getExampleByVpcId(vpcId));
    }

    @Override
    public List<OcCloudVpcSecurityGroup> queryOcCloudVpcSecurityGroupByVpcId(String vpcId) {
        return ocCloudVpcSecurityGroupMapper.selectByExample(getExampleByVpcId(vpcId));
    }

    @Override
    public OcCloudVpcSecurityGroup queryOcCloudVpcSecurityGroupById(int id) {
        return ocCloudVpcSecurityGroupMapper.selectByPrimaryKey(id);
    }

    @Override
    public OcCloudVpcSecurityGroup queryOcCloudVpcSecurityGroupBySecurityGroupId(String securityGroupId) {
        Example example = new Example(OcCloudVpcSecurityGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("securityGroupId", securityGroupId);
        return ocCloudVpcSecurityGroupMapper.selectOneByExample(example);
    }

    @Override
    public void deleteOcCloudVpcSecurityGroupById(int id) {
        ocCloudVpcSecurityGroupMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void addOcCloudVpcSecurityGroup(OcCloudVpcSecurityGroup ocCloudVpcSecurityGroup) {
        ocCloudVpcSecurityGroupMapper.insert(ocCloudVpcSecurityGroup);
    }

    @Override
    public void updateOcCloudVpcSecurityGroup(OcCloudVpcSecurityGroup ocCloudVpcSecurityGroup) {
        ocCloudVpcSecurityGroupMapper.updateByPrimaryKey(ocCloudVpcSecurityGroup);
    }

    private Example getExampleByVpcId(String vpcId) {
        Example example = new Example(OcCloudVpcSecurityGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("vpcId", vpcId);
        return example;
    }
}
