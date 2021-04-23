package com.baiyi.opscloud.service.dubbo.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcDubboTcpMapping;
import com.baiyi.opscloud.mapper.opscloud.OcDubboTcpMappingMapper;
import com.baiyi.opscloud.service.dubbo.OcDubboTcpMappingService;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/10/14 10:48 上午
 * @Version 1.0
 */
@Service
public class OcDubboTcpMappingServiceImpl implements OcDubboTcpMappingService {

    @Resource
    private OcDubboTcpMappingMapper ocDubboTcpMappingMapper;

    @Override
    public OcDubboTcpMapping queryOcDubboTcpMappingByMaxPort() {
        Example example = new Example(OcDubboTcpMapping.class);
        example.setOrderByClause(" tcp_port DESC");
        PageHelper.startPage(1, 1);
        return ocDubboTcpMappingMapper.selectOneByExample(example);
    }

    @Override
    public OcDubboTcpMapping queryOcDubboTcpMappingByUniqueKey(OcDubboTcpMapping ocDubboTcpMapping) {
        Example example = new Example(OcDubboTcpMapping.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("env", ocDubboTcpMapping.getEnv());
        criteria.andEqualTo("name", ocDubboTcpMapping.getName());
        return ocDubboTcpMappingMapper.selectOneByExample(example);
    }

    @Override
    public List<OcDubboTcpMapping> queryOcDubboTcpMappingByEnv(String env) {
        Example example = new Example(OcDubboTcpMapping.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("env", env);
        return ocDubboTcpMappingMapper.selectByExample(example);
    }

    @Override
    public void addOcDubboTcpMapping(OcDubboTcpMapping ocDubboTcpMapping) {
        ocDubboTcpMappingMapper.insert(ocDubboTcpMapping);
    }

    @Override
    public void updateOcDubboTcpMapping(OcDubboTcpMapping ocDubboTcpMapping) {
        ocDubboTcpMappingMapper.updateByPrimaryKey(ocDubboTcpMapping);
    }
}
