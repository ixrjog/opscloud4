package com.baiyi.opscloud.service.cloud.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudImage;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudInstanceType;
import com.baiyi.opscloud.domain.param.cloud.CloudInstanceTypeParam;
import com.baiyi.opscloud.mapper.opscloud.OcCloudInstanceTypeMapper;
import com.baiyi.opscloud.service.cloud.OcCloudInstanceTypeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/23 1:03 下午
 * @Version 1.0
 */
@Service
public class OcCloudInstanceTypeServiceImpl implements OcCloudInstanceTypeService {

    @Resource
    private OcCloudInstanceTypeMapper ocCloudInstanceTypeMapper;

    @Override
    public DataTable<OcCloudInstanceType> fuzzyQueryOcCloudInstanceTypeByParam(CloudInstanceTypeParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcCloudInstanceType> list = ocCloudInstanceTypeMapper.fuzzyQueryOcCloudInstanceTypeByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public List<OcCloudInstanceType> queryOcCloudInstanceTypeByType(int cloudType) {
        Example example = new Example(OcCloudImage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cloudType", cloudType);
        return ocCloudInstanceTypeMapper.selectByExample(example);
    }

    @Override
    public List<Integer> queryCpuCoreGroup(int cloudType) {
       return ocCloudInstanceTypeMapper.queryCpuCoreGroup(cloudType);
    }

    @Override
    public void addOcCloudInstanceType(OcCloudInstanceType ocCloudInstanceType) {
        ocCloudInstanceTypeMapper.insert(ocCloudInstanceType);
    }

    @Override
    public void updateOcCloudInstanceType(OcCloudInstanceType ocCloudInstanceType) {
        ocCloudInstanceTypeMapper.updateByPrimaryKey(ocCloudInstanceType);
    }

    @Override
    public OcCloudInstanceType queryOcCloudInstanceById(int id) {
        return ocCloudInstanceTypeMapper.selectByPrimaryKey(id);
    }

    @Override
    public void deleteOcCloudInstanceById(int id) {
        ocCloudInstanceTypeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public OcCloudInstanceType queryOcCloudInstanceByUniqueKey(OcCloudInstanceType ocCloudInstanceType) {
        return ocCloudInstanceTypeMapper.selectOne(ocCloudInstanceType);
    }
}
