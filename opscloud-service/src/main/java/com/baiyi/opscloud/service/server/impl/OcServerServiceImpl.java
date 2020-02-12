package com.baiyi.opscloud.service.server.impl;

import com.baiyi.opscloud.domain.generator.OcServer;
import com.baiyi.opscloud.mapper.OcServerMapper;
import com.baiyi.opscloud.service.server.OcServerService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/1/10 1:25 下午
 * @Version 1.0
 */
@Service
public class OcServerServiceImpl implements OcServerService {

    @Resource
    private OcServerMapper ocServerMapper;

    @Override
    public OcServer queryOcServerByPrivateIp(String privateIp) {
        Example example = new Example(OcServer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("privateIp", privateIp);
        return ocServerMapper.selectOneByExample(example);
    }
}
