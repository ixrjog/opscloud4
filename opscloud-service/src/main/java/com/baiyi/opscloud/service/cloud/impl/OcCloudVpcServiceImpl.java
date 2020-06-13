package com.baiyi.opscloud.service.cloud.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudVpc;
import com.baiyi.opscloud.domain.param.cloud.CloudVPCParam;
import com.baiyi.opscloud.mapper.opscloud.OcCloudVpcMapper;
import com.baiyi.opscloud.service.cloud.OcCloudVpcService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/18 7:15 下午
 * @Version 1.0
 */
@Service
public class OcCloudVpcServiceImpl implements OcCloudVpcService {

    @Resource
    private OcCloudVpcMapper ocCloudVpcMapper;

    @Override
    public DataTable<OcCloudVpc> fuzzyQueryOcCloudVpcByParam(CloudVPCParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcCloudVpc> ocCloudVpcList = ocCloudVpcMapper.fuzzyQueryOcCloudVpcByParam(pageQuery);
        return new DataTable<>(ocCloudVpcList, page.getTotal());
    }

    @Override
    public List<OcCloudVpc> queryOcCloudVpcByType(int cloudType) {
        Example example = new Example(OcCloudVpc.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cloudType", cloudType);
        return ocCloudVpcMapper.selectByExample(example);
    }

    @Override
    public OcCloudVpc queryOcCloudVpcById(int id) {
        return ocCloudVpcMapper.selectByPrimaryKey(id);
    }

    @Override
    public OcCloudVpc queryOcCloudVpcByVpcId(String vpcId) {
        Example example = new Example(OcCloudVpc.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("vpcId", vpcId);
        return ocCloudVpcMapper.selectOneByExample(example);
    }

    @Override
    public void deleteOcCloudVpcById(int id) {
        ocCloudVpcMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void addOcCloudVpc(OcCloudVpc ocCloudVpc) {
        ocCloudVpcMapper.insert(ocCloudVpc);
    }

    @Override
    public void updateOcCloudVpc(OcCloudVpc ocCloudVpc) {
        ocCloudVpcMapper.updateByPrimaryKey(ocCloudVpc);
    }
}
