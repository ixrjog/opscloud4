package com.baiyi.opscloud.service.server.impl;

import com.baiyi.opscloud.domain.generator.OcEnv;
import com.baiyi.opscloud.mapper.OcEnvMapper;
import com.baiyi.opscloud.service.server.OcEnvService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/1/10 2:17 下午
 * @Version 1.0
 */
@Service
public class OcEnvServiceImpl implements OcEnvService {

    @Resource
    private OcEnvMapper ocEnvMapper;

    @Override
    public OcEnv queryOcEnvById(Integer id) {
        return ocEnvMapper.selectByPrimaryKey(id);
    }

    @Override
    public OcEnv queryOcEnvByName(String name) {
        Example example = new Example(OcEnv.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("envName", name);
        return ocEnvMapper.selectOneByExample(example);

    }

}
