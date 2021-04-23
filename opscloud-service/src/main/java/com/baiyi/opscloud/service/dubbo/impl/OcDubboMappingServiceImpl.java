package com.baiyi.opscloud.service.dubbo.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcDubboMapping;
import com.baiyi.opscloud.mapper.opscloud.OcDubboMappingMapper;
import com.baiyi.opscloud.service.dubbo.OcDubboMappingService;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/10/9 4:10 下午
 * @Version 1.0
 */
@Service
public class OcDubboMappingServiceImpl implements OcDubboMappingService {

    @Resource
    private OcDubboMappingMapper ocDubboMappingMapper;

    @Override
    public OcDubboMapping queryOcDubboMappingById(int id) {
        return ocDubboMappingMapper.selectByPrimaryKey(id);
    }

    @Override
    public OcDubboMapping queryOneOcDubboUnmappedByEnv(String env) {
        Example example = new Example(OcDubboMapping.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("env", env);
        criteria.andEqualTo("tcpMappingId", 0);
        PageHelper.startPage(1, 1);
        return ocDubboMappingMapper.selectOneByExample(example);
    }

    @Override
    public List<OcDubboMapping> queryOcDubboMappingByEnv(String env) {
        Example example = new Example(OcDubboMapping.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("env", env);
        return ocDubboMappingMapper.selectByExample(example);
    }

    @Override
    public List<OcDubboMapping> queryOcDubboMappingByTcpMappingId(int tcpMappingId) {
        Example example = new Example(OcDubboMapping.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("tcpMappingId", tcpMappingId);
        return ocDubboMappingMapper.selectByExample(example);
    }

    @Override
    public OcDubboMapping queryOneOcDubboMappingByTcpMappingId(int tcpMappingId) {
        Example example = new Example(OcDubboMapping.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("tcpMappingId", tcpMappingId);
        PageHelper.startPage(1, 1);
        return ocDubboMappingMapper.selectOneByExample(example);
    }


    @Override
    public void addOcDubboMapping(OcDubboMapping ocDubboMapping) {
        ocDubboMappingMapper.insert(ocDubboMapping);
    }

    @Override
    public void updateOcDubboMapping(OcDubboMapping ocDubboMapping) {
        ocDubboMappingMapper.updateByPrimaryKey(ocDubboMapping);
    }

    @Override
    public OcDubboMapping qeuryOcDubboMappingByUniqueKey(OcDubboMapping ocDubboMapping) {
        Example example = new Example(OcDubboMapping.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("env", ocDubboMapping.getEnv());
        criteria.andEqualTo("dubboInterface", ocDubboMapping.getDubboInterface());
        return ocDubboMappingMapper.selectOneByExample(example);
    }
}
